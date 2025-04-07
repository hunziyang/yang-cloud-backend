package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.service.impl.userService.UserDto;
import com.yang.portal.user.service.impl.userService.UserSelectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends YangMapper<User> {
    long getUsersByDept(@Param("deptId") Long deptId);

    List<UserDto> select(@Param("query") UserSelectVo userSelectVo,
                         @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
                         @Param("sortList") List<Pagination.Sort> sorts);

    Long count(@Param("query") UserSelectVo userSelectVo);

    List<UserDto> selectByTenant(
            @Param("query") UserSelectVo userSelectVo,
            @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,
            @Param("sortList") List<Pagination.Sort> sorts, @Param("tenantId") Long tenantId);

    Long countByTenant(@Param("query") UserSelectVo userSelectVo, @Param("tenantId") Long tenantId);
}
