package com.yang.portal.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.page.PagedList;
import com.yang.portal.core.utils.ErrorMapUtil;
import com.yang.portal.core.utils.SqlOperateUtil;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import com.yang.portal.user.UserConstant;
import com.yang.portal.user.entity.Tenant;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.entity.UserRole;
import com.yang.portal.user.entity.UserTenant;
import com.yang.portal.user.mapper.*;
import com.yang.portal.user.service.RoleService;
import com.yang.portal.user.service.TenantService;
import com.yang.portal.user.service.UserService;
import com.yang.portal.user.service.impl.userService.*;
import com.yang.portal.user.utils.JwtUtil;
import com.yang.portal.user.utils.PasswordUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

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
        addUserRoles(user.getId(), userTenantCreateVo.getRoles(), false);
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

    private void addUserRoles(Long userId, List<Long> roleIds, boolean isDelete) {
        if (isDelete) {
            LambdaQueryWrapper<UserRole> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(UserRole::getUserId, userId)
                    .in(UserRole::getTenantId, UserContextHolder.getTenantId());
            userRoleMapper.delete(updateWrapper);
        }
        if (ObjectUtils.isNotEmpty(roleIds)) {
            if (!roleService.checkRolesInTenant(roleIds)) {
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
        addUserRoles(tenantAddExistUserVo.getUserId(), tenantAddExistUserVo.getRoles(), false);
    }

    @Override
    public UserLoginInfoDto login(UserLoginVo userLoginVo) {
        UserInfoDto userInfoDto = userMapper.getUserByUsername(userLoginVo.getUsername());
        if (ObjectUtils.isEmpty(userInfoDto)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        String password = PasswordUtil.getPassword(userLoginVo.getPassword(), userInfoDto.getSalt());
        if (!userInfoDto.getPassword().equals(password)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("密码错误"));
        }
        userInfoDto.setPassword(null);
        userInfoDto.setSalt(null);
        UserLoginInfoDto userLoginInfoDto = new UserLoginInfoDto();
        BeanUtils.copyProperties(userInfoDto, userLoginInfoDto);
        userLoginInfoDto.setPermissions(loginPermission());
        userLoginInfoDto.setJwtToken(JwtUtil.sign(userLoginInfoDto.getUsername()));
        redisTemplate.opsForValue().set(String.format(UserConstant.Jwt.REDIS_JWT_KEY, userLoginInfoDto.getJwtToken()), userLoginInfoDto, 2L, TimeUnit.MINUTES);
        return userLoginInfoDto;
    }

    private List<UserLoginInfoDto.UserLoginInfoPermissionDetail> loginPermission() {
        List<UserLoginInfoDto.UserLoginInfoPermissionDetail> userLoginInfoPermissionDetails = new ArrayList<>();
        UserLoginInfoDto.UserLoginInfoPermissionDetail tenantPermission = new UserLoginInfoDto.UserLoginInfoPermissionDetail();
        tenantPermission.setUrl("/user/user/getUserTenants");
        tenantPermission.setMethod("GET");
        userLoginInfoPermissionDetails.add(tenantPermission);
        UserLoginInfoDto.UserLoginInfoPermissionDetail tenantLoginPermission = new UserLoginInfoDto.UserLoginInfoPermissionDetail();
        tenantLoginPermission.setUrl("/user/user/tenantLogin/*");
        tenantLoginPermission.setMethod("POST");
        userLoginInfoPermissionDetails.add(tenantLoginPermission);
        return userLoginInfoPermissionDetails;
    }

    @Override
    public List<UserTenantDto> getUserTenants() {
        if (UserContextHolder.getUserId() == 1) {
            return tenantService.getAllTenants();
        } else {
            return userTenantMapper.getUserTenants(UserContextHolder.getUserId());
        }
    }

    @Override
    public UserLoginInfoDto tenantLogin(Long tenantId) {
        UserInfoDto userInfoDto = userMapper.getUserById(UserContextHolder.getUserId());
        if (ObjectUtils.isEmpty(userInfoDto)) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("用户不存在"));
        }
        UserLoginInfoDto userLoginInfoDto = new UserLoginInfoDto();
        BeanUtils.copyProperties(userInfoDto, userLoginInfoDto);
        userLoginInfoDto.setPassword(null);
        userLoginInfoDto.setSalt(null);
        setTenantInfo(userLoginInfoDto, tenantId);
        userLoginInfoDto.setJwtToken(JwtUtil.sign(userLoginInfoDto.getUsername()));
        setRoles(userLoginInfoDto, tenantId);
        setPermissions(userLoginInfoDto, tenantId);
        redisTemplate.opsForValue().set(String.format(UserConstant.Jwt.REDIS_JWT_KEY, userLoginInfoDto.getJwtToken()), userLoginInfoDto, 8L, TimeUnit.HOURS);
        return userLoginInfoDto;
    }

    private void setTenantInfo(UserLoginInfoDto userLoginInfoDto, Long tenantId) {
        if (UserContextHolder.getUserId() == 1) {
            Tenant tenant = tenantMapper.selectOneById(tenantId);
            if (ObjectUtils.isEmpty(tenant) || tenant.getStatus() == Boolean.TRUE) {
                throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前租户异常"));
            }
            userLoginInfoDto.setIsTenantAdmin(true);
        } else {
            TenantInfoDto tenantInfoDto = userTenantMapper.getTenantByTenantIdAndUserId(tenantId, UserContextHolder.getUserId());
            if (ObjectUtils.isEmpty(tenantInfoDto) || tenantInfoDto.getStatus() == Boolean.TRUE) {
                throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("当前租户异常"));
            }
        }
        userLoginInfoDto.setTenantId(tenantId);
    }

    private void setRoles(UserLoginInfoDto userLoginInfoDto, Long tenantId) {
        if (UserContextHolder.getUserId() == 1 || userLoginInfoDto.getIsTenantAdmin()) {
            userLoginInfoDto.setRoles(roleMapper.getRoleByTenantId(tenantId));
        } else {
            userLoginInfoDto.setRoles(userRoleMapper.getRoleInfoByTenantIdAndUserId(tenantId, UserContextHolder.getUserId()));
        }
    }

    private void setPermissions(UserLoginInfoDto userLoginInfoDto, Long tenantId) {
        if (userLoginInfoDto.getIsTenantAdmin() || UserContextHolder.getUserId() == 1) {
            userLoginInfoDto.setPermissions(permissionMapper.getTenantAdminPermissionByTenantId(tenantId));
        } else {
            userLoginInfoDto.setPermissions(permissionMapper.getUserPermissionByTenantId(tenantId, UserContextHolder.getUserId()));
        }
    }

    @Override
    public UserTenantRoleDto getUserTenantRoles(Long userId) {
        if (!(UserContextHolder.getUserId() == 1 || UserContextHolder.getIsAdmin())) {
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("无权限访问"));
        }
        return UserTenantRoleDto.builder()
                .userRoles(userRoleMapper.getRoleInfoByTenantIdAndUserId(UserContextHolder.getTenantId(), userId))
                .tenantRoles(roleMapper.getRoleByTenantId(UserContextHolder.getTenantId()))
                .build();
    }

    @Override
    public void updateUserRole(UpdateUserRoleVo updateUserRoleVo, Long userId) {
        addUserRoles(userId, updateUserRoleVo.getRoleIds(), true);
    }

    @Override
    public List<User> selectDeptUsers() {
        return userMapper.selectDeptUsers("testUser");
    }
}
