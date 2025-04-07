package com.yang.portal.user.typeHandle;

import com.yang.portal.user.entity.Permission;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionTypeHandle extends BaseTypeHandler<Permission.PermissionType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Permission.PermissionType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Permission.PermissionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return Permission.PermissionType.valueOf(value);
    }

    @Override
    public Permission.PermissionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return Permission.PermissionType.valueOf(value);
    }

    @Override
    public Permission.PermissionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return Permission.PermissionType.valueOf(value);
    }
}
