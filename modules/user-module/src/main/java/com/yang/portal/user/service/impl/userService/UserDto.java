package com.yang.portal.user.service.impl.userService;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String nickname;
    private ZonedDateTime birthday;
    private Boolean gender;
    private Long deptId;
    private String deptName;
    private Long postId;
    private String postName;
}
