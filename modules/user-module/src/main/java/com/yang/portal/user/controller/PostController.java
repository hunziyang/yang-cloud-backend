package com.yang.portal.user.controller;

import com.yang.portal.core.annotation.YangController;
import com.yang.portal.core.result.Result;
import com.yang.portal.user.service.PostService;
import com.yang.portal.user.service.impl.postService.PostSelectVo;
import com.yang.portal.user.service.impl.postService.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@YangController("/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 职位创建
     * @param postVo
     * @return
     */
    @PostMapping("/create")
    public Result create(@Validated @RequestBody PostVo postVo) {
        return Result.success(postService.create(postVo));
    }

    /**
     * 职位更新
     * @param postVo
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public Result update(@Validated @RequestBody PostVo postVo, @PathVariable("id") Long id) {
        postService.update(postVo, id);
        return Result.success();
    }

    /**
     * 职位删除
     * 只有当前职位没有人使用才能删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        postService.delete(id);
        return Result.success();
    }

    /**
     * 职位是否存在
     * @param name
     * @return
     */
    @GetMapping("/nameExist")
    public Result checkName(@RequestParam("name") String name) {
        return Result.success(postService.nameExist(name));
    }

    /**
     * 获取职位详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable("id") Long id) {
        return Result.success(postService.detail(id));
    }


    /**
     * 职位列表
     * @param postSelectVo
     * @return
     */
    @GetMapping("/select")
    public Result select(PostSelectVo postSelectVo) {
        return Result.success(postService.select(postSelectVo));
    }

}
