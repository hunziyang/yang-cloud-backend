package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.mapper.TenantMapper;
import com.yang.portal.user.mapper.UserTenantMapper;
import com.yang.portal.user.service.TenantService;
import com.yang.portal.user.service.impl.tenantService.TenantCreateVo;
import com.yang.portal.user.service.impl.tenantService.TenantSelectVo;
import com.yang.portal.user.service.impl.tenantService.TenantVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

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
    public void delete(Long id) {
        SqlOperateUtil.OperateSuccess(tenantMapper.uniqueKeyDelete(id));
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
}
