package com.yang.portal.cloud.core.userContext;

import lombok.Data;

@Data
public class UserContextHolder {

    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> DEPT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> JWT = new ThreadLocal<>();
    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setDeptId(Long userId) {
        DEPT_ID.set(userId);
    }

    public static Long getDeptId() {
        return DEPT_ID.get();
    }

    public static void setJwt(String jwt) {
        JWT.set(jwt);
    }

    public static String getJwt() {
        return JWT.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void close() {
        USERNAME.remove();
        USER_ID.remove();
        DEPT_ID.remove();
        JWT.remove();
        REQUEST_ID.remove();
    }
}
