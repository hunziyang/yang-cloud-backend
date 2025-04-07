package com.yang.portal.user.service;

import com.yang.portal.core.page.PagedList;
import com.yang.portal.user.entity.User;
import com.yang.portal.user.service.impl.userService.*;

public interface UserService {
    User create(UserCreateVo userCreateVo);

    void update(UserUpdateVo userUpdateVo, Long id);

    void delete(Long id);

    void restPassword(Long id, PasswordVo passwordVo);

    void userRestPassword(UserPasswordVo userPasswordVo);

    User tenantUserCreate(UserTenantCreateVo userTenantCreateVo);

    PagedList<UserDto> select(UserSelectVo userSelectVo);

    PagedList<UserDto> selectByTenant(UserSelectVo userSelectVo);

    void tenantAddExistUser(TenantAddExistUserVo tenantAddExistUserVo);
}
