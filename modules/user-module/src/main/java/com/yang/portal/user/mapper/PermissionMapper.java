package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.user.entity.Permission;
import com.yang.portal.user.service.impl.userService.UserLoginInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends YangMapper<Permission> {

    List<UserLoginInfoDto.UserLoginInfoPermissionDetail> getTenantAdminPermissionByTenantId(@Param("tenantId") Long tenantId);

    List<UserLoginInfoDto.UserLoginInfoPermissionDetail> getUserPermissionByTenantId(@Param("tenantId") Long tenantId, @Param("userId") Long userId);

    List<Permission> selectRouterByAdmin(@Param("tenantId") Long tenantId);
}
