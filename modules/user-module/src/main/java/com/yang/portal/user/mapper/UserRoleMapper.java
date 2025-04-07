package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.user.entity.UserRole;
import com.yang.portal.user.service.impl.userService.UserLoginInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends YangMapper<UserRole> {
    List<UserLoginInfoDto.UserLoginInfoRoleDetail> getRoleInfoByTenantIdAndUserId(@Param("tenantId") Long tenantId,@Param("userId") Long userId);
}
