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
     * @param userPasswordVo
     * @return
     */
    @PutMapping("/userRestPassword")
    public Result userRestPassword(@RequestBody @Validated UserPasswordVo userPasswordVo) {
        userService.userRestPassword(userPasswordVo);
        return Result.success();
    }

    /**
     * 非用户模块下创建用户(用户不存在)
     * @param userTenantCreateVo
     * @return
     */
    @PostMapping("/tenantUserCreate")
    public Result tenantUserCreate(@Validated @RequestBody UserTenantCreateVo userTenantCreateVo) {
        return Result.success(userService.tenantUserCreate(userTenantCreateVo));
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
    @GetMapping("/selectByTenant")
    public Result selectByTenant(UserSelectVo userSelectVo) {
        return Result.success(userService.selectByTenant(userSelectVo));
    }

    /**
     * 租户添加已经存在的用户
     * @return
     */
    @PostMapping("/tenantAddExistUser")
    public Result tenantAddExistUser(@RequestBody TenantAddExistUserVo tenantAddExistUserVo) {
        userService.tenantAddExistUser(tenantAddExistUserVo);
        return Result.success();
    }

    /**
     * 获取用户拥有租户
     * @return
     */
    @GetMapping("/getUserTenants")
    public Result getUserTenants() {
        return Result.success(userService.getUserTenants());
    }

    /**
     * 租户界面登录
     * @param tenantId
     * @return
     */
    @PostMapping("/tenantLogin/{tenantId}")
    public Result tenantLogin(@PathVariable("tenantId") Long tenantId) {
        return Result.success(userService.tenantLogin(tenantId));
    }

    /**
     * 获取用户租户下的角色
     * @param userId
     * @return
     */
    @GetMapping("/getUserTenantRoles/{id}")
    public Result getUserTenantRoles(@PathVariable("id") Long userId) {
        return Result.success(userService.getUserTenantRoles(userId));
    }

    /**
     * 更新用户角色
     * @param updateUserRoleVo
     * @param userId
     * @return
     */
    @PutMapping("/updateUserRole/{id}")
    public Result updateUserRole(@RequestBody UpdateUserRoleVo updateUserRoleVo, @PathVariable("id") Long userId) {
        userService.updateUserRole(updateUserRoleVo, userId);
        return Result.success();
    }

    @GetMapping("/selectDeptUser")
    public Result selectDeptUsers() {
        return Result.success(userService.selectDeptUsers());
    }
}
