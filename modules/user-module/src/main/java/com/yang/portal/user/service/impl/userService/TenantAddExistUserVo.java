package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TenantAddExistUserVo {

    @NotNull(message = "用户ID 不能为空")
    private Long userId;
    private List<Long> roles;
}
