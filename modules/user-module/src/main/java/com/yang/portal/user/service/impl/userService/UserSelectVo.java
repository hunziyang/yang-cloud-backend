package com.yang.portal.user.service.impl.userService;

import com.yang.portal.core.page.Pagination;
import lombok.Data;

@Data
public class UserSelectVo {

    private String nickname;
    private String username;
    private String deptId;
    private String postId;
    private Pagination pagination = new Pagination();
}
