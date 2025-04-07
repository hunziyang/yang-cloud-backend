package com.yang.portal.user.service.impl.deptService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeptVo {

    @NotNull(message = "父节点不能为空")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    private String name;

    private Boolean status = Boolean.FALSE;
}
