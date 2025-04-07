package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("DEPT")
@Accessors(chain = true)
public class Dept extends BaseEntity {

    /**
     * 父类 ID
     */
    private Long parentId;

    /**
     * 父类列表
     */
    private String ancestral;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门状态(false 可用， true 禁用)
     */
    private Boolean status;

    @TableField("UNIQUE_KEY")
    private Long uniqueKey;

}
