package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("POST")
@Accessors(chain = true)
public class Post extends BaseEntity {

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位描述
     */
    private String description;

    /**
     * 职位排序
     */
    @TableField("`ORDER`")
    private Integer order = 999;

    @TableField("UNIQUE_KEY")
    private Long uniqueKey;
}
