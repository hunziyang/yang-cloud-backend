package com.yang.portal.user.service.impl.postService;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostVo {

    @NotBlank(message = "职位名称不能为空")
    private String name;
    private String description;
    private Integer order = 999;
}
