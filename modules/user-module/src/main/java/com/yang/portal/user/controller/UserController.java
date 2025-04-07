package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.UserService;
import com.yang.portal.user.service.impl.userService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户模块创建
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody UserCreateVo userCreateVo) {
        return Result.success(userService.create(userCreateVo));
    }

    /**
     * 用户模块更新用户
     * @param userUpdateVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@Validated @RequestBody UserUpdateVo userUpdateVo, @PathVariable("id") Long id) {
        userService.update(userUpdateVo, id);
        return Result.success();
    }

    /**
     * 用户模块删除用户
     * 删除用户并删除对应的租户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return Result.success();
    }

    /**
     * 用户模块上的重置密码
     * @param id
     * @return
     */
    @PutMapping("/restPassword/{id}")
    public Result restPassword(@PathVariable("id") Long id, @RequestBody @Validated PasswordVo passwordVo) {
        userService.restPassword(id, passwordVo);
        return Result.success();
    }

    /**
     * 用户重置密码
     * @param id
     * @param userPasswordVo
     * @return
     */
    @PutMapping("/userRestPassword/{id}")
    public Result userRestPassword(@PathVariable("id") Long id, @RequestBody @Validated UserPasswordVo userPasswordVo) {
        userService.userRestPassword(id, userPasswordVo);
        return Result.success();
    }

    /**
     * 非用户模块下创建用户
     * @param userCreateVo
     * @return
     */
    @PostMapping("/moduleUserCreate")
    public Result moduleUserCreate(@Validated @RequestBody UserCreateVo userCreateVo) {
        return Result.success(userService.moduleUserCreate(userCreateVo));
    }

    /**
     * 获取全部用户
     * @param userSelectVo
     * @return
     */
    @GetMapping("/select")
    public Result select(UserSelectVo userSelectVo) {
        return Result.success(userService.select(userSelectVo));
    }

    /**
     * 获取租户下的用户
     * @param userSelectVo
     * @return
     */
    @GetMapping("/selectByModule")
    public Result selectByModule(UserSelectVo userSelectVo) {
        return Result.success(userService.selectByModule(userSelectVo));
    }
}
