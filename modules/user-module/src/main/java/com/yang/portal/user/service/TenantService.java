package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.service.impl.tenantService.TenantCreateVo;
import com.yang.portal.user.service.impl.tenantService.TenantSelectVo;
import com.yang.portal.user.service.impl.tenantService.TenantVo;

public interface TenantService {
    Tenant create(TenantCreateVo tenantCreateVo);

    void update(TenantVo tenantVo, Long id);

    void delete(Long id);

    PagedList<Tenant> select(TenantSelectVo tenantSelectVo);
}
