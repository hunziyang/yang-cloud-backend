package com.yang.portal.user.service.impl.roleService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RoleVo {

    @NotBlank(message = "名称不能为空")
    private String name;

    private List<Long> permissionIds;
}
