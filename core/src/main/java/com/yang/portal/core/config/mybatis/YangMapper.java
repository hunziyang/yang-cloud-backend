package com.yang.portal.core.config.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface YangMapper<T> extends BaseMapper<T> {

    default int delete(Long id) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ID", id)
                .eq("IS_DELETED", false)
                .set("IS_DELETED", true);
        return update(updateWrapper);
    }

    default int uniqueKeyDelete(Long id) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ID", id)
                .eq("IS_DELETED", false)
                .set("IS_DELETED", true)
                .set("UNIQUE_KEY", id);
        return update(updateWrapper);
    }

    default T selectOneById(Long id){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ID", id)
                .eq("IS_DELETED", false);
        return selectOne(queryWrapper);
    }
}
