package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.user.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends YangMapper<User> {
    long getUsersByDept(@Param("deptId") Long deptId);
}
