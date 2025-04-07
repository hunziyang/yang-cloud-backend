package com.yang.portal.user.service.impl.roleService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

@Data
public class RoleSelectVo {

    private String name;
    private Pagination pagination = new Pagination();
}
