package com.yang.portal.user.service.impl.permissionService;

import com.yang.portal.user.entity.Permission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionTreeDto {

    private Permission permission;
    private List<PermissionTreeDto> children;
}
