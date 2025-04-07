package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.user.entity.Post;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.mapper.PostMapper;
import com.yang.portal.user.mapper.UserMapper;
import com.yang.portal.user.service.PostService;
import com.yang.portal.user.service.impl.postService.PostSelectVo;
import com.yang.portal.user.service.impl.postService.PostVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Post create(PostVo postVo) {
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        postMapper.insert(post);
        return post;
    }

    @Override
    public void update(PostVo postVo, Long id) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getId, id)
                .eq(Post::getIsDeleted, false)
                .set(Post::getName, postVo.getName())
                .set(Post::getDescription, postVo.getDescription())
                .set(Post::getOrder, postVo.getOrder())
                .set(Post::getUpdatedTime, ZonedDateTime.now(ZoneId.systemDefault()));
        SqlOperateUtil.OperateSuccess(postMapper.update(updateWrapper));
    }

    @Override
    public void delete(Long id) {
        if (checkPostUserExist(id)){
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前职位正在被使用"));
        }
        SqlOperateUtil.OperateSuccess(postMapper.uniqueKeyDelete(id));
    }

    private boolean checkPostUserExist(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPostId, id)
                .eq(User::getIsDeleted, false);
        return ObjectUtils.isNotEmpty(userMapper.selectList(queryWrapper));
    }

    @Override
    public Boolean nameExist(String name) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getName, name)
                .eq(Post::getUniqueKey, 0);
        return ObjectUtils.isNotEmpty(postMapper.selectOne(queryWrapper));
    }

    @Override
    public Post detail(Long id) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getId, id)
                .eq(Post::getIsDeleted, false);
        return postMapper.selectOne(queryWrapper);
    }

    @Override
    public PagedList<Post> select(PostSelectVo postSelectVo) {
        List<Post> result  = postMapper.select(
                postSelectVo,
                postSelectVo.getPagination().getOffset(), postSelectVo.getPagination().getPageSize(),
                postSelectVo.getPagination().getSorts());
        Long count = postMapper.count(postSelectVo);
        return PagedList.<Post>builder()
                .result(result)
                .count(count)
                .pageNum(postSelectVo.getPagination().getPageNum())
                .pageSize(postSelectVo.getPagination().getPageSize())
                .build();
    }
}
