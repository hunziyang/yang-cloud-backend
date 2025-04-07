package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordVo {

    @NotBlank(message = "密码不能为空")
    private String password;
}
