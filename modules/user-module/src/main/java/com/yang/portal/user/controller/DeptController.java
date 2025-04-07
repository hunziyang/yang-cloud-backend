package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.DeptService;
import com.yang.portal.user.service.impl.deptService.DeptNameCheckVo;
import com.yang.portal.user.service.impl.deptService.DeptSelectVo;
import com.yang.portal.user.service.impl.deptService.DeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 部门创建
     * @param deptVo
     * @return
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody DeptVo deptVo) {
        deptService.create(deptVo);
        return Result.success();
    }

    /**
     * 部门更新
     * 部门关闭需要子部门全部关闭
     * 部门打开需要父部门全部打开
     * @param deptVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@Validated @RequestBody DeptVo deptVo, @PathVariable("id") Long id) {
        deptService.update(deptVo, id);
        return Result.success();
    }

    /**
     * 部门删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        deptService.delete(id);
        return Result.success();
    }

    /**
     * 获取部门组织列表（含根目录）
     */
    @GetMapping("/deptTreeContainRoot/{id}")
    public Result deptTreeContainRoot(@PathVariable("id") Long id) {
        return Result.success(deptService.deptTreeContainRoot(id));
    }

    /**
     * 获取部门组织列表
     */
    @GetMapping("/deptTree/{id}")
    public Result deptTree(@PathVariable("id") Long id) {
        return Result.success(deptService.deptTree(id));
    }

    /**
     * 获取部门及其子部门
     */
    @GetMapping("/deptAndChild/{id}")
    public Result deptAndChild(@PathVariable("id") Long id) {
        return Result.success(deptService.deptAndChild(id));
    }

    /**
     * 部门查询
     * @param deptSelectVo
     * @return
     */
    @GetMapping("/select")
    public Result select(@Validated DeptSelectVo deptSelectVo) {
        return Result.success(deptService.select(deptSelectVo));
    }

    /**
     * 部门详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long id) {
        return Result.success(deptService.detail(id));
    }

    /**
     * 检查部门名称是否占用
     * @param deptNameCheckVo
     * @return
     */
    @GetMapping("/nameExist")
    public Result nameExist(DeptNameCheckVo deptNameCheckVo) {
        return Result.success(deptService.nameExist(deptNameCheckVo));
    }

}
