package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.Post;
import com.yang.portal.user.service.impl.postService.PostSelectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper extends YangMapper<Post> {
    List<Post> select(
            @Param("query") PostSelectVo postSelectVo,
            @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
            @Param("sortList") List<Pagination.Sort> sorts);

    Long count(@Param("query") PostSelectVo postSelectVo);
}
