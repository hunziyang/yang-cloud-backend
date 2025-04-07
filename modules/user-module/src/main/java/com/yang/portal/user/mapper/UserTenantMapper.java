package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.service.impl.tenantService.TenantUserDto;
import com.yang.portal.user.service.impl.tenantService.TenantUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTenantMapper extends YangMapper<UserTenant> {

    List<TenantUserDto> tenantUser(@Param("query") TenantUserVo tenantUserVo,
                                   @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
                                   @Param("sortList") List<Pagination.Sort> sorts);

    Long tenantUserCount(@Param("query") TenantUserVo tenantUserVo);
}
