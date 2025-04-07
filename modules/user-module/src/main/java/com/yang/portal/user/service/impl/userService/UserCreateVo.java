package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Data
public class UserCreateVo {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;
    private ZonedDateTime birthday;

    /**
     * false: 男 ，true: 女
     */
    private Boolean gender;
    private Long deptId;
    private Long postId;
}
