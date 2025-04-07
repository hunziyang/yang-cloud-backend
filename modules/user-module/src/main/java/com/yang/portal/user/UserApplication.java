package com.yang.portal.user;

import com.yang.portal.core.annotation.YangApplication;
import com.yang.portal.core.utils.SpringUtil;
import com.yang.portal.currentUserInfo.annotation.EnableCurrentUserInfo;

@YangApplication
@EnableCurrentUserInfo
public class UserApplication {

    public static void main(String[] args) {
        SpringUtil.run(UserApplication.class, args);
    }
}
