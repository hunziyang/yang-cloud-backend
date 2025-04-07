package com.yang.portal.user.anonymousController;

import com.yang.portal.core.annotation.AnonymousController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.UserService;
import com.yang.portal.user.service.impl.userService.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AnonymousController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 只验证账号是否正确，跳转至选择租户界面
     * @param userLoginVo
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody UserLoginVo userLoginVo) {
        return Result.success(userService.login(userLoginVo));
    }
}
