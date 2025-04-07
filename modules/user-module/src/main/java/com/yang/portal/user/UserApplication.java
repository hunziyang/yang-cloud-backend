package com.yang.portal.user;

import com.yang.portal.core.annotation.YangApplication;
import com.yang.portal.core.utils.SpringUtil;
import com.yang.portal.currentUserInfo.annotation.EnableCurrentUserInfo;
import com.yang.portal.dataScope.annotation.EnableCurrentUserDataScope;

@YangApplication
@EnableCurrentUserInfo
@EnableCurrentUserDataScope
public class UserApplication {

    public static void main(String[] args) {
        SpringUtil.run(UserApplication.class, args);
    }
}
