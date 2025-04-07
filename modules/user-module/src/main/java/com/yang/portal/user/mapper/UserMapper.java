package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.dataScope.annotation.DataScope;
import com.yang.portal.dataScope.annotation.DataScopeColumn;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.service.impl.userService.UserDto;
import com.yang.portal.user.service.impl.userService.UserInfoDto;
import com.yang.portal.user.service.impl.userService.UserSelectVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    UserInfoDto getUserByUsername(@Param("username") String username);

    UserInfoDto getUserById(@Param("userId")Long userId);

    @DataScope(
            dataScopeColumns = {
                    @DataScopeColumn(column = "ID",dataScopeType = "MYSELF"),
                    @DataScopeColumn(column = "DEPT_ID",dataScopeType = "DEPT"),
                    @DataScopeColumn(column = "DEPT_ID",dataScopeType = "DEPT_AND_CHILD")
            }
    )
    @Select("SELECT * FROM USER where username = #{username}")
    List<User> selectDeptUsers(@Param("username") String username);

}
