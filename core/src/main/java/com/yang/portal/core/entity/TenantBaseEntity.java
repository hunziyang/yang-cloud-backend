package com.yang.portal.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class TenantBaseEntity extends BaseEntity {

    @TableField("TENANT_ID")
    private Long tenantId;
}
