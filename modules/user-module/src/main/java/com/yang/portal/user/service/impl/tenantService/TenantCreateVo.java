package com.yang.portal.user.service.impl.tenantService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class TenantCreateVo {

    @NotBlank(message = "租户名称不能为空")
    private String name;
    private String description;
    private Boolean status = Boolean.FALSE;

    @NotEmpty(message = "租户管理员不能为空")
    private List<Long> userId;
}
