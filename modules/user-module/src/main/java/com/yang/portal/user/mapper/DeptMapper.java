package com.yang.portal.user.mapper;

import com.yang.portal.core.config.mybatis.YangMapper;
import com.yang.portal.core.page.Pagination;
import com.yang.portal.user.entity.Dept;
import com.yang.portal.user.service.impl.deptService.DeptSelectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper extends YangMapper<Dept> {
    Long getChildOpenStatusCount(@Param("id") Long id);

    List<Dept> findChildDept(@Param("id") Long id);

    Long getChildCount(Long id);

    List<Dept> getDeptAndChild(Long id);

    List<Dept> select(@Param("query") DeptSelectVo deptSelectVo, @Param("offset") Integer pageNum, @Param("size") Integer pageSize, @Param("sortList") List<Pagination.Sort> sorts);

    Long count(@Param("query") DeptSelectVo deptSelectVo);
}
