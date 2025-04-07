package com.yang.portal.user.service.impl.userService;

import lombok.Data;

@Data
public class UserPasswordVo {

    private String oldPassword;
    private String newPassword;
}
