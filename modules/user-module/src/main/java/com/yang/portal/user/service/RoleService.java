package com.yang.portal.user.service;

import com.yang.portal.user.entity.Role;
import com.yang.portal.user.service.impl.roleService.RoleVo;

public interface RoleService {
    Role create(RoleVo roleVo);

    void update(RoleVo roleVo, Long id);

    void delete(Long id);
}
