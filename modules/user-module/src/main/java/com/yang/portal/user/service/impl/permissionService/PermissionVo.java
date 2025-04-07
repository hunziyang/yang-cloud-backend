package com.yang.portal.user.service.impl.permissionService;

import com.yang.portal.user.entity.Permission;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PermissionVo {

    @NotNull(message ="父级目录不能为空")
    private Long parentId;

    @NotBlank(message = "权限名称不能为空")
    private String name;

    private String url;

    @NotNull(message = "权限类型不能为空")
    private Permission.PermissionType type;

    private Permission.RequestMethod method =  Permission.RequestMethod.NOT_REQUIRED;
    private String route;
}
