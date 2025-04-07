package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginInfoDto extends UserInfoDto{

    private Long tenantId;
    private Boolean isTenantAdmin = Boolean.FALSE;
    private List<UserLoginInfoRoleDetail> roles;
    private List<UserLoginInfoPermissionDetail> permissions;
    private String jwtToken;

    @Data
    public static class UserLoginInfoRoleDetail{
        private Long id;
        private String name;
    }

    @Data
    public static class UserLoginInfoPermissionDetail{
        private String method;
        private String url;
        private String scope;
    }

}
