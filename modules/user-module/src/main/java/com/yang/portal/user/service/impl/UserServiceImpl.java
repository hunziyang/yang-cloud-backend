package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.entity.UserRole;
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.mapper.UserMapper;
import com.yang.portal.user.mapper.UserRoleMapper;
import com.yang.portal.user.mapper.UserTenantMapper;
import com.yang.portal.user.service.RoleService;
import com.yang.portal.user.service.UserService;
import com.yang.portal.user.service.impl.userService.*;
import com.yang.portal.user.utils.PasswordUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public User create(UserCreateVo userCreateVo) {
        User user = new User();
        BeanUtils.copyProperties(userCreateVo, user);
        user.setSalt(PasswordUtil.getSalt());
        user.setPassword(PasswordUtil.getPassword(userCreateVo.getPassword(), user.getSalt()));
        userMapper.insert(user);
        user.setPassword(null)
                .setSalt(null);
        return user;
    }

    @Override
    public void update(UserUpdateVo userUpdateVo, Long id) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(User::getNickname, userUpdateVo.getNickname())
                .set(User::getBirthday, userUpdateVo.getBirthday())
                .set(User::getGender, userUpdateVo.getGender())
                .set(User::getPostId, userUpdateVo.getPostId())
                .set(User::getDeptId, userUpdateVo.getDeptId());
        SqlOperateUtil.OperateSuccess(userMapper.update(updateWrapper));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LambdaUpdateWrapper<User> userUpdateWrapper = new LambdaUpdateWrapper<>();
        userUpdateWrapper.eq(User::getId, id)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(User::getIsDeleted, !CoreConstant.DEFAULT_IS_DELETED);
        SqlOperateUtil.OperateSuccess(userMapper.update(userUpdateWrapper));
        LambdaQueryWrapper<UserTenant> userTenantWrapper = new LambdaQueryWrapper<>();
        userTenantWrapper.eq(UserTenant::getUserId, id);
        userTenantMapper.delete(userTenantWrapper);
    }

    @Override
    public void restPassword(Long id, PasswordVo passwordVo) {
        User user = getUserById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .set(User::getPassword, PasswordUtil.getPassword(passwordVo.getPassword(), user.getSalt()));
        SqlOperateUtil.OperateSuccess(userMapper.update(updateWrapper));
    }

    @Override
    public void userRestPassword(UserPasswordVo userPasswordVo) {
        User user = getUserById(UserContextHolder.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, UserContextHolder.getUserId())
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(User::getPassword, PasswordUtil.getPassword(userPasswordVo.getOldPassword(), user.getSalt()))
                .set(User::getPassword, PasswordUtil.getPassword(userPasswordVo.getNewPassword(), user.getSalt()));
        SqlOperateUtil.OperateSuccess(userMapper.update(updateWrapper));
    }

    @Override
    @Transactional
    public User tenantUserCreate(UserTenantCreateVo userTenantCreateVo) {
        User user = create(userTenantCreateVo);
        UserTenant userTenant = new UserTenant()
                .setUserId(user.getId())
                .setTenantId(UserContextHolder.getTenantId());
        userTenantMapper.insert(userTenant);
        if (ObjectUtils.isNotEmpty(userTenantCreateVo.getRoles())){
            addUserRoles(user.getId(), userTenantCreateVo.getRoles());
        }
        return user;
    }

    @Override
    public PagedList<UserDto> select(UserSelectVo userSelectVo) {
        List<UserDto> result = userMapper.select(
                userSelectVo,
                userSelectVo.getPagination().getOffset(), userSelectVo.getPagination().getPageSize(),
                userSelectVo.getPagination().getSorts());
        Long count = userMapper.count(userSelectVo);
        return PagedList.<UserDto>builder()
                .result(result)
                .count(count)
                .pageNum(userSelectVo.getPagination().getPageNum())
                .pageSize(userSelectVo.getPagination().getPageSize())
                .build();
    }

    @Override
    public PagedList<UserDto> selectByTenant(UserSelectVo userSelectVo) {
        List<UserDto> result = userMapper.selectByTenant(
                userSelectVo,
                userSelectVo.getPagination().getOffset(), userSelectVo.getPagination().getPageSize(),
                userSelectVo.getPagination().getSorts(), UserContextHolder.getTenantId());
        Long count = userMapper.countByTenant(userSelectVo, UserContextHolder.getTenantId());
        return PagedList.<UserDto>builder()
                .result(result)
                .count(count)
                .pageNum(userSelectVo.getPagination().getPageNum())
                .pageSize(userSelectVo.getPagination().getPageSize())
                .build();
    }

    private User getUserById(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED);
        return userMapper.selectOne(queryWrapper);
    }

    private void addUserRoles(Long userId, List<Long> roleIds) {
        if (!roleService.checkRolesInTenant(roleIds)){
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("存在角色不属于当前模块"));
        }
        List<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setTenantId(UserContextHolder.getTenantId());
            userRoles.add(userRole);
        }
        userRoleMapper.insert(userRoles);
    }

    @Override
    @Transactional
    public void tenantAddExistUser(TenantAddExistUserVo tenantAddExistUserVo) {
        User user = getUserById(tenantAddExistUserVo.getUserId());
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        UserTenant userTenant = new UserTenant()
                .setUserId(tenantAddExistUserVo.getUserId())
                .setTenantId(UserContextHolder.getTenantId());
        userTenantMapper.insert(userTenant);
        if (ObjectUtils.isNotEmpty(tenantAddExistUserVo.getRoles())){
            addUserRoles(tenantAddExistUserVo.getUserId(), tenantAddExistUserVo.getRoles());
        }
    }
}
