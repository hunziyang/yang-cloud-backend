package com.yang.portal.user.service.impl.roleService;

import com.yang.portal.user.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleVo {

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "数据范围不能为空")
    private Role.RoleScope scope;

    private List<Long> permissionIds;
}
