package com.yang.portal.user.entity;

import lombok.Data;

@Data
public class RolePermission {

    private Long roleId;
    private Long permissionId;
    private Long tenantId;
}
