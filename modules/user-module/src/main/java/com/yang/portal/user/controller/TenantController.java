package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.TenantService;
import com.yang.portal.user.service.impl.tenantService.TenantCreateVo;
import com.yang.portal.user.service.impl.tenantService.TenantSelectVo;
import com.yang.portal.user.service.impl.tenantService.TenantVo;
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
    @PostMapping
    public Result create(@Validated @RequestBody TenantCreateVo tenantCreateVo) {
        return Result.success(tenantService.create(tenantCreateVo));
    }

    /**
     * 租户更新
     * @param tenantVo
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@Validated @RequestBody TenantVo tenantVo, @PathVariable("id") Long id) {
        tenantService.update(tenantVo, id);
        return Result.success();
    }

    /**
     * 租户删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    /**
     * 租户查询
     * @param tenantSelectVo
     * @return
     */
    @GetMapping
    public Result select(TenantSelectVo tenantSelectVo) {
        return Result.success(tenantService.select(tenantSelectVo));
    }



}
