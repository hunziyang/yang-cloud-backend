package com.yang.portal.currentUserInfo.contextHolder;

import lombok.Data;

@Data
public class UserContextHolder {

    private static final ThreadLocal<String> NICKNAME = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> AUTHORIZATION = new ThreadLocal<>();
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> DEPT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> POST_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IS_ADMIN = new ThreadLocal<>();


    public static void setNickname(String nickname) {
        NICKNAME.set(nickname);
    }

    public static String getNickname() {
        return NICKNAME.get();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setAuthorization(String authorization) {
        AUTHORIZATION.set(authorization);
    }

    public static String getAuthorization() {
        return AUTHORIZATION.get();
    }

    public static void setTenantId(Long accountId) {
        TENANT_ID.set(accountId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setDeptId(Long deptId) {
        DEPT_ID.set(deptId);
    }

    public static Long getDeptId() {
        return DEPT_ID.get();
    }

    public static Long getPostId() {
        return POST_ID.get();
    }
    public static void setPostId(Long postId) {
        POST_ID.set(postId);
    }

    public static Boolean getIsAdmin() {
        return IS_ADMIN.get();
    }
    public static void setIsAdmin(Boolean isAdmin) {
        IS_ADMIN.set(isAdmin);
    }

    public static void close() {
        NICKNAME.remove();
        USER_ID.remove();
        AUTHORIZATION.remove();
        TENANT_ID.remove();
        DEPT_ID.remove();
        POST_ID.remove();
    }
}
