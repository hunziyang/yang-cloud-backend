package com.yang.portal.user.service.impl.userService;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserTenantRoleDto {

    private List<UserLoginInfoDto.UserLoginInfoRoleDetail> userRoles;
    private List<UserLoginInfoDto.UserLoginInfoRoleDetail> tenantRoles;
}
