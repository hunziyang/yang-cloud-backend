package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@TableName("USER")
@Accessors(chain = true)
public class User extends BaseEntity {

    private String username;
    private String password;
    private String salt;
    private String nickname;
    private ZonedDateTime birthday;
    private Boolean gender;
    private Long deptId;
    private Long postId;

    @TableField("UNIQUE_KEY")
    private Long uniqueKey;
}
