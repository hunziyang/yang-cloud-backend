package com.yang.portal.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("IS_DELETED")
    private Boolean isDeleted = Boolean.FALSE;

    @TableField("CREATED_BY")
    private Long createdBy;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private ZonedDateTime createdTime;

    @TableField("UPDATED_BY")
    private Long updatedBy;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.UPDATE)
    private ZonedDateTime updatedTime;
}
