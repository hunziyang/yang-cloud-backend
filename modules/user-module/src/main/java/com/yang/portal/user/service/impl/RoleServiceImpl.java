package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.entity.Role;
import com.yang.portal.user.entity.RolePermission;
import com.yang.portal.user.mapper.RoleMapper;
import com.yang.portal.user.mapper.RolePermissionMapper;
import com.yang.portal.user.service.RoleService;
import com.yang.portal.user.service.impl.roleService.RoleVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional
    public Role create(RoleVo roleVo) {
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        role.setTenantId(UserContextHolder.getTenantId());
        roleMapper.insert(role);
        if (ObjectUtils.isNotEmpty(roleVo.getPermissionIds())) {
            insertRolePermission(roleVo.getPermissionIds(), role.getId(), false);
        }
        return role;
    }

    private void insertRolePermission(List<Long> permissions, Long roleId, boolean isDelete) {
        if (isDelete) {
            deleteRolePermissions(roleId);
        }
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermission.setTenantId(UserContextHolder.getTenantId());
            rolePermissions.add(rolePermission);
        }
        rolePermissionMapper.insert(rolePermissions);
    }

    @Override
    @Transactional
    public void update(RoleVo roleVo, Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, id)
                .eq(Role::getTenantId, UserContextHolder.getTenantId())
                .eq(Role::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(Role::getName, roleVo.getName());
        SqlOperateUtil.OperateSuccess(roleMapper.update(updateWrapper));
        if (ObjectUtils.isNotEmpty(roleVo.getPermissionIds())) {
            insertRolePermission(roleVo.getPermissionIds(), id, true);
        }
    }

    @Override
    public void delete(Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, id)
                .eq(Role::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(Role::getTenantId, UserContextHolder.getTenantId())
                .set(Role::getIsDeleted, !CoreConstant.DEFAULT_IS_DELETED);
        SqlOperateUtil.OperateSuccess(roleMapper.update(updateWrapper));
        deleteRolePermissions(id);
    }

    private void deleteRolePermissions(Long roleId) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getTenantId, UserContextHolder.getTenantId());
        rolePermissionMapper.delete(queryWrapper);
    }
}
