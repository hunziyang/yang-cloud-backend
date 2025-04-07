package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.PermissionService;
import com.yang.portal.user.service.impl.permissionService.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    /**
     * 权限创建
     * @param permissionVo
     * @return
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody PermissionVo permissionVo) {
        return Result.success(permissionService.create(permissionVo));
    }

    /**
     * 权限更新
     * @param permissionVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@Validated @RequestBody PermissionVo permissionVo, @PathVariable("id") Long id) {
        permissionService.update(permissionVo,id);
        return Result.success();
    }

    /**
     * 权限查询
     * 只包含当前租户下的权限
     * @return
     */
    @GetMapping("/permissionTree/{id}")
    public Result permissionTree() {
        return Result.success(permissionService.permissionTree());
    }

}
