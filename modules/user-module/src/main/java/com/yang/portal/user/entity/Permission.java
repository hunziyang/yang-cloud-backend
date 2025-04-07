package com.yang.portal.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.portal.core.entity.TenantBaseEntity;
import com.yang.portal.user.typeHandle.PermissionTypeHandle;
import com.yang.portal.user.typeHandle.RequestMethodHandle;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("PERMISSION")
public class Permission extends TenantBaseEntity {

    private Long parentId;
    private String name;
    private String url;

    @TableField(typeHandler = PermissionTypeHandle.class)
    private PermissionType type;

    @TableField(typeHandler = RequestMethodHandle.class)
    private RequestMethod method = RequestMethod.NOT_REQUIRED;
    private String route;

    public enum PermissionType {
        CATALOG,
        MENU,
        BUTTON
    }

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE,
        NOT_REQUIRED
    }
}
