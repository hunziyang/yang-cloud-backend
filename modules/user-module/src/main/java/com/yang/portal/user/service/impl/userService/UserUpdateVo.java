package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Data
public class UserUpdateVo {

    @NotBlank(message = "昵称不能为空")
    private String nickname;
    private ZonedDateTime birthday;
    private Boolean gender;
    private Long deptId;
    private Long postId;
}
