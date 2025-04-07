package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.entity.Permission;
import com.yang.portal.user.mapper.PermissionMapper;
import com.yang.portal.user.service.PermissionService;
import com.yang.portal.user.service.impl.permissionService.PermissionTreeDto;
import com.yang.portal.user.service.impl.permissionService.PermissionVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Permission create(PermissionVo permissionVo) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVo, permission);
        permission.setTenantId(UserContextHolder.getTenantId());
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    public void update(PermissionVo permissionVo, Long id) {
        if (permissionVo.getParentId() == id) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("父级目录不能是自己"));
        }
        LambdaUpdateWrapper<Permission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Permission::getTenantId, UserContextHolder.getTenantId())
                .eq(Permission::getId, id)
                .eq(Permission::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(Permission::getParentId, permissionVo.getParentId())
                .set(Permission::getName, permissionVo.getName())
                .set(Permission::getUrl, permissionVo.getUrl())
                .set(Permission::getType, permissionVo.getType())
                .set(Permission::getMethod, permissionVo.getMethod())
                .set(Permission::getRoute, permissionVo.getRoute());
        SqlOperateUtil.OperateSuccess(permissionMapper.update(updateWrapper));
    }

    @Override
    public List<PermissionTreeDto> permissionTree() {
        List<Permission> permissionList = getPermission();
        Map<Long, List<Permission>> parentIdPermissionMap = permissionList.stream().collect(Collectors.groupingBy(Permission::getParentId));
        List<PermissionTreeDto> permissionTreeDtoList = new ArrayList<>();
        List<Permission> permissions = parentIdPermissionMap.get(0L);
        for (Permission permission : permissions) {
            PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
            permissionTreeDto.setPermission(permission);
            permissionTreeDto.setChildren(getChildPermission(permission, parentIdPermissionMap));
            permissionTreeDtoList.add(permissionTreeDto);
        }
        return permissionTreeDtoList;
    }

    private List<PermissionTreeDto> getChildPermission(Permission permission, Map<Long, List<Permission>> parentIdPermissionMap) {
        List<Permission> permissions = parentIdPermissionMap.get(permission.getId());
        if (ObjectUtils.isEmpty(permissions)) {
            return null;
        }
        List<PermissionTreeDto> permissionTreeDtoList = new ArrayList<>();
        for (Permission childPermission : permissions) {
            PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
            permissionTreeDto.setPermission(childPermission);
            permissionTreeDto.setChildren(getChildPermission(childPermission, parentIdPermissionMap));
            permissionTreeDtoList.add(permissionTreeDto);
        }
        return permissionTreeDtoList;
    }

    private List<Permission> getPermission() {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(Permission::getTenantId, UserContextHolder.getTenantId());
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public boolean checkPermissionsInTenant(List<Long> permissions) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getId, permissions)
                .eq(Permission::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(Permission::getTenantId, UserContextHolder.getTenantId());
        return permissionMapper.selectCount(queryWrapper) == permissions.size();
    }

    @Override
    public Permission detail(Long id) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getId, id)
                .eq(Permission::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(Permission::getTenantId, UserContextHolder.getTenantId());
        return permissionMapper.selectOne(queryWrapper);
    }

    @Override
    public List<PermissionTreeDto> routerTree() {
        List<Permission> permissionList = getRouterPermissionList();
        Map<Long, List<Permission>> parentIdPermissionMap = permissionList.stream().collect(Collectors.groupingBy(Permission::getParentId));
        List<Permission> permissions = parentIdPermissionMap.get(0L);
        List<PermissionTreeDto> permissionTreeDtoList = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
            permissionTreeDto.setPermission(permission);
            permissionTreeDto.setChildren(getChildPermission(permission, parentIdPermissionMap));
            permissionTreeDtoList.add(permissionTreeDto);
        }
        return permissionTreeDtoList;
    }

    private List<Permission> getRouterPermissionList() {
        List<Permission> permissionList;
        if (UserContextHolder.getIsAdmin() || UserContextHolder.getUserId() == 1) {
            permissionList = permissionMapper.selectRouterByAdmin(UserContextHolder.getTenantId());
        } else {
            LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Permission::getTenantId, UserContextHolder.getTenantId())
                    .eq(Permission::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                    .in(Permission::getType, Arrays.asList("CATALOG", "MENU"));
            permissionList = permissionMapper.selectList(queryWrapper);
        }
        return permissionList;
    }

}
