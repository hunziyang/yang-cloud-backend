package com.yang.portal.user.service.impl.tenantService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

@Data
public class TenantSelectVo {

    private String name;
    private String description;
    private Boolean status;
    private Pagination pagination = new Pagination();
}
