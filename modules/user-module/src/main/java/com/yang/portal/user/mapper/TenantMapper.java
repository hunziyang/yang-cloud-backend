package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.service.impl.tenantService.TenantSelectVo;
import com.yang.portal.user.service.impl.userService.UserTenantDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenantMapper extends YangMapper<Tenant> {
    List<Tenant> select(
            @Param("query") TenantSelectVo tenantSelectVo,
            @Param("offset") Integer offset, @Param("size") Integer pageSize,
            @Param("sortList") List<Pagination.Sort> sorts);

    Long count(@Param("query") TenantSelectVo tenantSelectVo);

    List<UserTenantDto> getAllTenants();
}
