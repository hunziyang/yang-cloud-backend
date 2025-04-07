package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import java.util.List;

@Data
public class UserTenantCreateVo extends UserCreateVo {

    private List<Long> roles;
}
