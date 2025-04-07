package com.yang.portal.user.typeHandle;

import com.yang.portal.user.entity.Permission;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestMethodHandle extends BaseTypeHandler<Permission.RequestMethod> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Permission.RequestMethod parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i , parameter.name());
    }

    @Override
    public Permission.RequestMethod getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return Permission.RequestMethod.valueOf(value);
    }

    @Override
    public Permission.RequestMethod getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return Permission.RequestMethod.valueOf(value);
    }

    @Override
    public Permission.RequestMethod getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return Permission.RequestMethod.valueOf(value);
    }
}
