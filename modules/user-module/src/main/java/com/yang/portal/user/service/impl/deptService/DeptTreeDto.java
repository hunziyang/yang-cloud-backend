package com.yang.portal.user.service.impl.deptService;

import com.yang.portal.user.entity.Dept;
import lombok.Data;

import java.util.List;

@Data
public class DeptTreeDto {

    // 当前部门信息
    private Dept dept;

    private List<DeptTreeDto> children;
}
