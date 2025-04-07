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
        List<Permission> permissions = parentIdPermissionMap.get(0);
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


}
