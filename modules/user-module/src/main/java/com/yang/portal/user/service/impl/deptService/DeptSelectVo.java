package com.yang.portal.user.service.impl.deptService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

import javax.validation.Valid;

@Data
public class DeptSelectVo {

    private String name;
    private Boolean status;
    private Long parentId;

    @Valid
    private Pagination pagination = new Pagination();
}
