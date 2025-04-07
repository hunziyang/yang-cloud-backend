package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ROLE_PERMISSION")
public class RolePermission {

    private Long roleId;
    private Long permissionId;
    private Long tenantId;
}
