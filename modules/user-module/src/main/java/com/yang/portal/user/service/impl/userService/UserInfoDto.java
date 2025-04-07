package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserInfoDto {

    private Long id;
    private String username;
    private String password;
    private String salt;
    private String nickname;
    private ZonedDateTime birthday;
    private Boolean gender;
    private Long deptId;
    private Long postId;
    private String deptName;
    private String postName;
}
