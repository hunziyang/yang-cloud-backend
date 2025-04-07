package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.mapper.TenantMapper;
import com.yang.portal.user.mapper.UserMapper;
import com.yang.portal.user.mapper.UserTenantMapper;
import com.yang.portal.user.service.TenantService;
import com.yang.portal.user.service.impl.tenantService.*;
import com.yang.portal.user.service.impl.userService.UserTenantDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Tenant create(TenantCreateVo tenantCreateVo) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantCreateVo, tenant);
        tenantMapper.insert(tenant);
        List<UserTenant> userTenantList = new ArrayList<>();
        tenantCreateVo.getUserId().forEach(userId -> {
            UserTenant userTenant = new UserTenant()
                    .setTenantId(tenant.getId())
                    .setUserId(userId)
                    .setIsAdmin(true);
            userTenantList.add(userTenant);
        });
        userTenantMapper.insert(userTenantList);
        return tenant;
    }

    @Override
    public void update(TenantVo tenantVo, Long id) {
        LambdaUpdateWrapper<Tenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tenant::getId, id)
                .eq(Tenant::getIsDeleted, false)
                .set(Tenant::getName, tenantVo.getName())
                .set(Tenant::getStatus, tenantVo.getStatus())
                .set(Tenant::getDescription, tenantVo.getDescription());
        SqlOperateUtil.OperateSuccess(tenantMapper.update(updateWrapper));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SqlOperateUtil.OperateSuccess(tenantMapper.uniqueKeyDelete(id));
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getTenantId, id);
        userTenantMapper.delete(queryWrapper);
    }

    @Override
    public PagedList<Tenant> select(TenantSelectVo tenantSelectVo) {
        List<Tenant> result = tenantMapper.select(
                tenantSelectVo,
                tenantSelectVo.getPagination().getOffset(), tenantSelectVo.getPagination().getPageSize(),
                tenantSelectVo.getPagination().getSorts());
        Long count = tenantMapper.count(tenantSelectVo);
        return PagedList.<Tenant>builder()
                .result(result)
                .count(count)
                .pageNum(tenantSelectVo.getPagination().getPageNum())
                .pageSize(tenantSelectVo.getPagination().getPageSize())
                .build();
    }

    @Override
    public PagedList<TenantUserDto> tenantUser(TenantUserVo tenantUserVo) {
        List<TenantUserDto> result = userTenantMapper.tenantUser(tenantUserVo,
                tenantUserVo.getPagination().getOffset(), tenantUserVo.getPagination().getPageSize(),
                tenantUserVo.getPagination().getSorts());
        Long count = userTenantMapper.tenantUserCount(tenantUserVo);
        return PagedList.<TenantUserDto>builder()
                .result(result)
                .count(count)
                .pageNum(tenantUserVo.getPagination().getPageNum())
                .pageSize(tenantUserVo.getPagination().getPageSize())
                .build();
    }

    @Override
    public void updateTenantAdmin(TenantAdminVo tenantAdminVo) {
        LambdaUpdateWrapper<UserTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserTenant::getTenantId, tenantAdminVo.getTenantId())
                .eq(UserTenant::getUserId, tenantAdminVo.getUserId())
                .set(UserTenant::getIsAdmin, tenantAdminVo.getIsAdmin());
        SqlOperateUtil.OperateSuccess(userTenantMapper.update(updateWrapper));
    }

    @Override
    public void addTenantUser(TenantAdminVo tenantAdminVo) {
        if (ObjectUtils.isEmpty(getUserById(tenantAdminVo.getUserId()))) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        if (ObjectUtils.isEmpty(getTenantById(tenantAdminVo.getTenantId()))) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("租户存在"));
        }
        UserTenant userTenant = new UserTenant();
        BeanUtils.copyProperties(tenantAdminVo, userTenant);
        userTenantMapper.insert(userTenant);
    }

    @Override
    public void deleteTenantUser(TenantUserDeleteVo tenantUserDeleteVo) {
        LambdaQueryWrapper<UserTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTenant::getTenantId, tenantUserDeleteVo.getTenantId())
                .eq(UserTenant::getUserId, tenantUserDeleteVo.getUserId());
        SqlOperateUtil.OperateSuccess(userTenantMapper.delete(queryWrapper));
    }

    @Override
    public void addTenantUserByModule(Long userId) {
        TenantAdminVo tenantAdminVo = new TenantAdminVo();
        tenantAdminVo.setUserId(userId);
        tenantAdminVo.setTenantId(UserContextHolder.getTenantId());
        addTenantUser(tenantAdminVo);
    }

    public User getUserById(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED);
        return userMapper.selectOne(queryWrapper);
    }

    public Tenant getTenantById(Long tenantId) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getId, tenantId)
                .eq(Tenant::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED);
        return tenantMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserTenantDto> getAllTenants() {
        return tenantMapper.getAllTenants();
    }
}
