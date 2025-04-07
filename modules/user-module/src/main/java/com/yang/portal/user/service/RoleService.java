package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.Role;
import com.yang.portal.user.service.impl.roleService.RoleDetailDto;
import com.yang.portal.user.service.impl.roleService.RoleSelectVo;
import com.yang.portal.user.service.impl.roleService.RoleVo;

import java.util.List;

public interface RoleService {
    Role create(RoleVo roleVo);

    void update(RoleVo roleVo, Long id);

    void delete(Long id);

    boolean checkRolesInTenant(List<Long> roleIds);

    RoleDetailDto detail(Long id);

    PagedList<Role> select(RoleSelectVo roleSelectVo);
}
