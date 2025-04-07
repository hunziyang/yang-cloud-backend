package com.yang.portal.dataScope.interceptor;

import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.dataScope.DataScopeConstant;
import com.yang.portal.dataScope.annotation.DataScope;
import com.yang.portal.dataScope.annotation.DataScopeColumn;
import com.yang.portal.dataScope.dto.DataScopeColumnDto;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DataScopeInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.SELECT) {
            selectOperation(metaObject, mappedStatement, statementHandler);
        }
        return invocation.proceed();
    }

    private void selectOperation(MetaObject metaObject, MappedStatement mappedStatement, StatementHandler statementHandler) throws Throwable {
        if (UserContextHolder.getIsAdmin() || UserContextHolder.getUserId() == 1L) {
            return;
        }
        if (UserContextHolder.getDataScope().contains(DataScopeConstant.DATA_SCOPE_TYPE_ALL)) {
            return;
        }
        Map<String, DataScopeColumnDto> dataScopeTypeDataMap = getDataScopeTypeDataMap(mappedStatement);
        if (ObjectUtils.isEmpty(dataScopeTypeDataMap)) {
            return;
        }
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        List<String> dataScopeList = Arrays.stream(UserContextHolder.getDataScope().split(",")).collect(Collectors.toList());
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = select.getPlainSelect();
        if (dataScopeList.size() == 1) {
            updateWhere(dataScopeList.get(0), plainSelect, dataScopeTypeDataMap);
        } else {
            updateWhere(dataScopeList, plainSelect, dataScopeTypeDataMap);
        }
        BoundSql newBoundSql = new BoundSql(
                mappedStatement.getConfiguration(),
                plainSelect.toString(),
                boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        metaObject.setValue("delegate.boundSql", newBoundSql);
    }

    private Map<String, DataScopeColumnDto> getDataScopeTypeDataMap(MappedStatement mappedStatement) throws Throwable {
        DataScope dataScope = getDataScope(mappedStatement);
        if (ObjectUtils.isEmpty(dataScope)) {
            return null;
        }
        DataScopeColumn[] dataScopeColumns = dataScope.dataScopeColumns();
        if (ObjectUtils.isEmpty(dataScopeColumns)) {
            return null;
        }
        return Arrays.stream(dataScopeColumns).map(dataScopeColumn ->
                DataScopeColumnDto.builder()
                        .column(dataScopeColumn.column())
                        .dataScopeType(dataScopeColumn.dataScopeType())
                        .tableAlias(dataScopeColumn.tableAlias())
                        .build()
        ).collect(Collectors.toMap(DataScopeColumnDto::getDataScopeType, dataScopeColumn -> dataScopeColumn));
    }

    private DataScope getDataScope(MappedStatement mappedStatement) throws Throwable {
        String methodFullPath = mappedStatement.getId();
        String classFullPath = methodFullPath.substring(0, methodFullPath.lastIndexOf("."));
        String methodName = methodFullPath.substring(methodFullPath.lastIndexOf(".") + 1);
        Class clazz = Class.forName(classFullPath);
        Method method = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> methodName.equals(m.getName())).findFirst().orElse(null);
        if (ObjectUtils.isEmpty(method)) {
            return null;
        }
        return (DataScope) Arrays.stream(method.getDeclaredAnnotations())
                .filter(annotation -> annotation instanceof DataScope).findFirst().orElse(null);
    }

    private void updateWhere(String dataScope, PlainSelect plainSelect, Map<String, DataScopeColumnDto> dataScopeTypeDataMap) {
        DataScopeColumnDto dataScopeColumnDto = dataScopeTypeDataMap.get(dataScope);
        if (ObjectUtils.isEmpty(dataScopeColumnDto)) {
            throw new RuntimeException("数据权限不存在");
        }
        String column = getColumn(dataScopeColumnDto.getTableAlias(), dataScopeColumnDto.getColumn());
        Expression expression = plainSelect.getWhere();
        if ("MYSELF".equals(dataScope)) {
            setLongWhere(column, UserContextHolder.getUserId(), expression, plainSelect);
        }
        if ("DEPT".equals(dataScope)) {
            setLongWhere(column, UserContextHolder.getDeptId(), expression, plainSelect);
        }
        if ("DEPT_AND_CHILD".equals(dataScope)) {
            setInWhere(column, expression, plainSelect);
        }
    }

    private String getColumn(String tableAlias, String tableColumn) {
        String column;
        if (StringUtils.isNotBlank(tableAlias)) {
            column = String.format("%s.%s", tableAlias, tableColumn);
        } else {
            column = tableColumn;
        }
        return column;
    }

    private void setLongWhere(String column, Long value, Expression expression, PlainSelect plainSelect) {
        EqualsTo equalsTo = getEqualsToLong(value, column);
        if (ObjectUtils.isEmpty(expression)) {
            plainSelect.setWhere(equalsTo);
        } else {
            AndExpression andExpression = new AndExpression(expression, equalsTo);
            plainSelect.setWhere(andExpression);
        }
    }

    private EqualsTo getEqualsToLong(Long value, String column) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(column));
        equalsTo.setRightExpression(new LongValue(value));
        return equalsTo;
    }

    private void setInWhere(String column, Expression expression, PlainSelect plainSelect) {
        InExpression inExpression = getInExpression(column);
        if (ObjectUtils.isEmpty(expression)) {
            plainSelect.setWhere(inExpression);
        } else {
            AndExpression andExpression = new AndExpression(expression, inExpression);
            plainSelect.setWhere(andExpression);
        }
    }

    private InExpression getInExpression(String column) {
        GreaterThan findInSetExpression = getFindInSetExpression();
        EqualsTo equalsTo = getEqualsToLong(UserContextHolder.getDeptId(), "ID");
        OrExpression childOrExpression = new OrExpression(findInSetExpression, equalsTo);
        PlainSelect childSelect = new PlainSelect();
        childSelect.setSelectItems(Collections.singletonList(new SelectItem(new Column("ID"))));
        childSelect.setFromItem(new Table("DEPT"));
        childSelect.setWhere(childOrExpression);
        return new InExpression(new Column(column), new Parenthesis(childSelect));
    }

    private GreaterThan getFindInSetExpression() {
        Function function = new Function();
        function.setName("FIND_IN_SET");
        function.setParameters(new ExpressionList(
                new LongValue(UserContextHolder.getDeptId()),
                new Column("ANCESTRAL")
        ));
        GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(function);
        greaterThan.setRightExpression(new LongValue(0));
        return greaterThan;
    }

    private void updateWhere(List<String> dataScopeList, PlainSelect plainSelect, Map<String, DataScopeColumnDto> dataScopeTypeDataMap) {
        Expression expression = plainSelect.getWhere();
        OrExpression baseExpression = getBaseExpression(dataScopeList, dataScopeTypeDataMap);
        if (dataScopeList.size() > 2){
            baseExpression = new OrExpression(baseExpression, getExpression(dataScopeList.get(2), dataScopeTypeDataMap));
        }
        if (ObjectUtils.isEmpty(expression)) {
            plainSelect.setWhere(baseExpression);
        } else {
            AndExpression andExpression = new AndExpression(expression, new Parenthesis(baseExpression));
            plainSelect.setWhere(andExpression);
        }
    }

    private OrExpression getBaseExpression(List<String> dataScopeList, Map<String, DataScopeColumnDto> dataScopeTypeDataMap) {
        String leftDataScope = dataScopeList.get(0);
        String rightDataScope = dataScopeList.get(1);
        OrExpression orExpression = new OrExpression(getExpression(leftDataScope, dataScopeTypeDataMap), getExpression(rightDataScope, dataScopeTypeDataMap));
        return orExpression;
    }

    private Expression getExpression(String dataScope, Map<String, DataScopeColumnDto> dataScopeTypeDataMap) {
        DataScopeColumnDto dataScopeColumnDto = dataScopeTypeDataMap.get(dataScope);
        if (ObjectUtils.isEmpty(dataScopeColumnDto)) {
            throw new RuntimeException("数据权限不存在");
        }
        String column = getColumn(dataScopeColumnDto.getTableAlias(), dataScopeColumnDto.getColumn());
        if ("MYSELF".equals(dataScope)) {
            return getEqualsToLong(UserContextHolder.getUserId(), column);
        } else if ("DEPT".equals(dataScope)) {
            return getEqualsToLong(UserContextHolder.getDeptId(), column);
        } else if ("DEPT_AND_CHILD".equals(dataScope)) {
            return getInExpression(column);
        } else {
            throw new RuntimeException("dataScope 不存在");
        }
    }
}
