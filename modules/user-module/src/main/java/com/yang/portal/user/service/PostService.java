package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.Post;
import com.yang.portal.user.service.impl.postService.PostSelectVo;
import com.yang.portal.user.service.impl.postService.PostVo;

public interface PostService {
    Post create(PostVo postVo);

    void update(PostVo postVo, Long id);

    void delete(Long id);

    Boolean nameExist(String name);

    Post detail(Long id);

    PagedList<Post> select(PostSelectVo postSelectVo);
}
