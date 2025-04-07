package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.service.impl.tenantService.*;
import com.yang.portal.user.service.impl.userService.UserTenantDto;

import java.util.List;

public interface TenantService {
    Tenant create(TenantCreateVo tenantCreateVo);

    void update(TenantVo tenantVo, Long id);

    void delete(Long id);

    PagedList<Tenant> select(TenantSelectVo tenantSelectVo);

    PagedList<TenantUserDto> tenantUser(TenantUserVo tenantUserVo);

    void updateTenantAdmin(TenantAdminVo tenantAdminVo);

    void addTenantUser(TenantAdminVo tenantAdminVo);

    void deleteTenantUser(TenantUserDeleteVo tenantUserDeleteVo);

    void addTenantUserByModule(Long userId);

    List<UserTenantDto> getAllTenants();
}
