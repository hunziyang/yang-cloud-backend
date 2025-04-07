package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.RoleService;
import com.yang.portal.user.service.impl.roleService.RoleSelectVo;
import com.yang.portal.user.service.impl.roleService.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色创建
     * @param roleVo
     * @return
     */
    @PostMapping("/create")
    public Result create(@RequestBody @Validated RoleVo roleVo) {
        return Result.success(roleService.create(roleVo));
    }

    /**
     * 角色更新
     * @param roleVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@RequestBody @Validated RoleVo roleVo, @PathVariable("id") Long id) {
        roleService.update(roleVo, id);
        return Result.success();
    }

    /**
     * 角色删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        roleService.delete(id);
        return Result.success();
    }

    /**
     * 获取角色详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long id) {
        return Result.success(roleService.detail(id));
    }

    /**
     * 角色查询
     * @param roleSelectVo
     * @return
     */
    @GetMapping("/select")
    public Result select(RoleSelectVo roleSelectVo) {
        return Result.success(roleService.select(roleSelectVo));
    }

}
