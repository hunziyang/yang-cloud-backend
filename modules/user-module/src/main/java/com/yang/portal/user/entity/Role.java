package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.TenantBaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("ROLE")
public class Role extends TenantBaseEntity {

    private String name;

    private RoleScope scope;

    @TableField("UNIQUE_KEY")
    private Long uniqueKey;

    public enum RoleScope {
        ALL, DEPT, DEPT_AND_CHILD, MYSELF;
    }
}
