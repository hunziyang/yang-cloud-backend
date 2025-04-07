package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.entity.Role;
import com.yang.portal.user.entity.RolePermission;
import com.yang.portal.user.mapper.RoleMapper;
import com.yang.portal.user.mapper.RolePermissionMapper;
import com.yang.portal.user.service.PermissionService;
import com.yang.portal.user.service.RoleService;
import com.yang.portal.user.service.impl.permissionService.PermissionTreeDto;
import com.yang.portal.user.service.impl.roleService.RoleDetailDto;
import com.yang.portal.user.service.impl.roleService.RoleSelectVo;
import com.yang.portal.user.service.impl.roleService.RoleVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional
    public Role create(RoleVo roleVo) {
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        role.setTenantId(UserContextHolder.getTenantId());
        roleMapper.insert(role);
        insertRolePermission(roleVo.getPermissionIds(), role.getId(), false);
        return role;
    }

    private void insertRolePermission(List<Long> permissions, Long roleId, boolean isDelete) {
        if (isDelete) {
            deleteRolePermissions(roleId);
        }
        if (ObjectUtils.isNotEmpty(permissions)) {
            if (!permissionService.checkPermissionsInTenant(permissions)) {
                throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("存在权限不属于当前租户"));
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
    }

    @Override
    @Transactional
    public void update(RoleVo roleVo, Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, id)
                .eq(Role::getTenantId, UserContextHolder.getTenantId())
                .eq(Role::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(Role::getName, roleVo.getName())
                .set(Role::getScope, roleVo.getScope());
        SqlOperateUtil.OperateSuccess(roleMapper.update(updateWrapper));
        insertRolePermission(roleVo.getPermissionIds(), id, true);
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


    @Override
    public boolean checkRolesInTenant(List<Long> roleIds) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId, roleIds)
                .eq(Role::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(Role::getTenantId, UserContextHolder.getTenantId());
        return roleMapper.selectCount(queryWrapper) == roleIds.size();
    }

    @Override
    public RoleDetailDto detail(Long id) {
        LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(Role::getId, id)
                .eq(Role::getTenantId, UserContextHolder.getTenantId())
                .eq(Role::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED);
        Role role = roleMapper.selectOne(roleQueryWrapper);
        if (ObjectUtils.isEmpty(role)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前角色不存在"));
        }
        LambdaQueryWrapper<RolePermission> rolePermissionQueryWrapper = new LambdaQueryWrapper<>();
        rolePermissionQueryWrapper.eq(RolePermission::getRoleId, id)
                .eq(RolePermission::getTenantId, UserContextHolder.getTenantId());
        List<Long> permissionIds = rolePermissionMapper.selectList(rolePermissionQueryWrapper)
                .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<PermissionTreeDto> permissionTreeDtoList = permissionService.permissionTree();
        return RoleDetailDto.builder()
                .permissionIds(permissionIds)
                .permissionTreeDtoList(permissionTreeDtoList)
                .build();
    }

    @Override
    public PagedList<Role> select(RoleSelectVo roleSelectVo) {
        List<Role> result = roleMapper.select(roleSelectVo,
                roleSelectVo.getPagination().getOffset(), roleSelectVo.getPagination().getPageSize(), roleSelectVo.getPagination().getSorts(),
                UserContextHolder.getTenantId());
        Long count = roleMapper.count(roleSelectVo, UserContextHolder.getTenantId());
        return PagedList.<Role>builder()
                .result(result)
                .count(count)
                .pageNum(roleSelectVo.getPagination().getPageNum())
                .pageSize(roleSelectVo.getPagination().getPageSize())
                .build();
    }
}
