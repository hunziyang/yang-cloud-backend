package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("USER_TENANT")
@Accessors(chain = true)
public class UserTenant {

    private Long userId;
    private Long tenantId;
    private Boolean isAdmin = Boolean.FALSE;
}
