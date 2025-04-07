package com.yang.portal.user.service.impl.roleService;

import com.yang.portal.user.entity.Role;
import com.yang.portal.user.service.impl.permissionService.PermissionTreeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleDetailDto {

    private Role role;
    private List<Long> permissionIds;
    private List<PermissionTreeDto> permissionTreeDtoList;
}
