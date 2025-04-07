package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.Dept;
import com.yang.portal.user.service.impl.deptService.DeptNameCheckVo;
import com.yang.portal.user.service.impl.deptService.DeptSelectVo;
import com.yang.portal.user.service.impl.deptService.DeptTreeDto;
import com.yang.portal.user.service.impl.deptService.DeptVo;

import java.util.List;

public interface DeptService {
    void create(DeptVo deptVo);

    void update(DeptVo deptVo, Long id);

    void delete(Long id);

    List<DeptTreeDto> deptTreeContainRoot(Long id);

    List<Dept> deptAndChild(Long id);

    PagedList<Dept> select(DeptSelectVo deptSelectVo);

    Dept detail(Long id);

    Boolean nameExist(DeptNameCheckVo deptNameCheckVo);

    List<DeptTreeDto> deptTree(Long id);
}
