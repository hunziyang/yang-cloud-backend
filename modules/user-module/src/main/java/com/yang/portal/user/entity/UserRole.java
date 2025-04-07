package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("USER_ROLE")
public class UserRole {

    private Long userId;
    private Long roleId;
    private Long tenantId;
}
