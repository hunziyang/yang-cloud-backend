package com.yang.portal.user.service.impl.tenantService;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TenantVo {

    @NotBlank(message = "租户名称不能为空")
    private String name;
    private String description;
    private Boolean status = Boolean.FALSE;
}
