package com.yang.portal.user.service.impl.tenantService;

import lombok.Data;

@Data
public class TenantUserDto {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否是租户管理员
     */
    private Boolean isAdmin;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 职务
     */
    private String postName;
}
