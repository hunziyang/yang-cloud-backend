package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("TENANT")
@Accessors(chain = true)
public class Tenant extends BaseEntity {

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户状态
     */
    private Boolean status;

    /**
     * 租户描述
     */
    private String description;

    @TableField("UNIQUE_KEY")
    private Long uniqueKey;
}
