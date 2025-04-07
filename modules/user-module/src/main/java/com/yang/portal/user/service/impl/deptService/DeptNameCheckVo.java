package com.yang.portal.user.service.impl.deptService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeptNameCheckVo {

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "父部门不能为空")
    private Long parentId;
}
