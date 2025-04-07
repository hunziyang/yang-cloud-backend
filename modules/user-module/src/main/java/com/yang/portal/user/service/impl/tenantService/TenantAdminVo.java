package com.yang.portal.user.service.impl.tenantService;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TenantAdminVo {

    @NotNull(message = "租户 ID 不能为空")
    private Long tenantId;

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @NotNull(message = "是否是系统管理员状态不能为空")
    private Boolean isAdmin = Boolean.FALSE;
}
