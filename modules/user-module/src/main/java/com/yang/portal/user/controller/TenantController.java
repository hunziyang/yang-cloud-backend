package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.TenantService;
import com.yang.portal.user.service.impl.tenantService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * 租户创建并设置租户管理员
     * @param tenantCreateVo
     * @return
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody TenantCreateVo tenantCreateVo) {
        return Result.success(tenantService.create(tenantCreateVo));
    }

    /**
     * 租户更新
     * @param tenantVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@Validated @RequestBody TenantVo tenantVo, @PathVariable("id") Long id) {
        tenantService.update(tenantVo, id);
        return Result.success();
    }

    /**
     * 租户删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    /**
     * 租户查询
     * @param tenantSelectVo
     * @return
     */
    @GetMapping("/select")
    public Result select(TenantSelectVo tenantSelectVo) {
        return Result.success(tenantService.select(tenantSelectVo));
    }

    /**
     * 获取租户下的用户
     * @param tenantUserVo
     * @return
     */
    @GetMapping("/tenantUser")
    public Result tenantUser(@Validated TenantUserVo tenantUserVo) {
        return Result.success(tenantService.tenantUser(tenantUserVo));
    }

    /**
     * 更新租户管理员状态
     * @param tenantAdminVo
     * @return
     */
    @PutMapping("/updateTenantAdmin")
    public Result updateTenantAdmin(@Validated @RequestBody TenantAdminVo tenantAdminVo) {
        tenantService.updateTenantAdmin(tenantAdminVo);
        return Result.success();
    }

    /**
     * 租户增加用户
     * @param tenantAdminVo
     * @return
     */
    @PostMapping("/addTenantUser")
    public Result addTenantUser(@Validated @RequestBody TenantAdminVo tenantAdminVo) {
        tenantService.addTenantUser(tenantAdminVo);
        return Result.success();
    }

    /**
     * 租户删除用户
     * @param tenantUserDeleteVo
     * @return
     */
    @DeleteMapping("/deleteTenantUser")
    public Result deleteTenantUser(@Validated @RequestBody TenantUserDeleteVo tenantUserDeleteVo) {
        tenantService.deleteTenantUser(tenantUserDeleteVo);
        return Result.success();
    }

    /**
     * 非用户模块租户增加用户
     * @param userId
     * @return
     */
    @PostMapping("/addTenantUserByModule/{id}")
    public Result addTenantUserByModule(@PathVariable("id") Long userId) {
        tenantService.addTenantUserByModule(userId);
        return Result.success();
    }

}
