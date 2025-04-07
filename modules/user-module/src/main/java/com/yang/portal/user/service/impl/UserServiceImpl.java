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
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.mapper.UserMapper;
import com.yang.portal.user.mapper.UserTenantMapper;
import com.yang.portal.user.service.UserService;
import com.yang.portal.user.service.impl.userService.*;
import com.yang.portal.user.utils.PasswordUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

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
    public void userRestPassword(Long id, UserPasswordVo userPasswordVo) {
        User user = getUserById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id)
                .eq(User::getIsDeleted, CoreConstant.DEFAULT_IS_DELETED)
                .eq(User::getPassword, PasswordUtil.getPassword(userPasswordVo.getOldPassword(), user.getSalt()))
                .set(User::getPassword, PasswordUtil.getPassword(userPasswordVo.getNewPassword(), user.getSalt()));
        SqlOperateUtil.OperateSuccess(userMapper.update(updateWrapper));
    }

    @Override
    @Transactional
    public User moduleUserCreate(UserCreateVo userCreateVo) {
        User user = create(userCreateVo);
        UserTenant userTenant = new UserTenant()
                .setUserId(user.getId())
                .setTenantId(UserContextHolder.getTenantId());
        userTenantMapper.insert(userTenant);
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
    public PagedList<UserDto> selectByModule(UserSelectVo userSelectVo) {
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
}
