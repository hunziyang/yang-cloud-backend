package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.Role;
import com.yang.portal.user.service.impl.roleService.RoleSelectVo;
import com.yang.portal.user.service.impl.userService.UserLoginInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends YangMapper<Role> {
    List<Role> select(@Param("query") RoleSelectVo roleSelectVo,
                      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
                      @Param("sortList") List<Pagination.Sort> sorts, @Param("tenantId") Long tenantId);

    Long count(@Param("query") RoleSelectVo roleSelectVo, @Param("tenantId") Long tenantId);

    List<UserLoginInfoDto.UserLoginInfoRoleDetail> getRoleByTenantId(@Param("tenantId") Long tenantId);
}
