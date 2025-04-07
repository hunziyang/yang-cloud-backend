package com.yang.portal.user.service.impl.tenantService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TenantUserVo {

    @NotNull(message = "租户 ID 不能为空")
    private Long id;
    private String username;
    private String nickname;
    private Long deptId;
    private Long postId;
    private Boolean isAdmin;
    private Pagination pagination = new Pagination();
}
