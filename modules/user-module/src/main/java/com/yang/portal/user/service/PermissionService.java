package com.yang.portal.user.service;

import com.yang.portal.user.entity.Permission;
import com.yang.portal.user.service.impl.permissionService.PermissionTreeDto;
import com.yang.portal.user.service.impl.permissionService.PermissionVo;

import java.util.List;

public interface PermissionService {
    Permission create(PermissionVo permissionVo);

    void update(PermissionVo permissionVo, Long id);

    List<PermissionTreeDto> permissionTree();

    boolean checkPermissionsInTenant(List<Long> permissions);

    Permission detail(Long id);

    List<PermissionTreeDto> routerTree();


}
