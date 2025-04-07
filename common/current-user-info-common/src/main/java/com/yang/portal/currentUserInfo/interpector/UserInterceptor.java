package com.yang.portal.currentUserInfo.interpector;

import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private static final String NICKNAME = "NICKNAME";
    private static final String DEFAULT_NICKNAME = "DEFAULT-NICKNAME";
    private static final String USER_ID = "USER-ID";
    private static final Long DEFAULT_USER_ID = -1L;
    private static final String TENANT_ID = "TENANT-ID";
    private static final Long DEFAULT_TENANT_ID = -1L;
    private static final String DEPT_ID = "DEPT-ID";
    private static final Long DEFAULT_DEPT_ID = -1L;
    private static final String POST_ID = "POST-ID";
    private static final Long DEFAULT_POST_ID = -1L;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String nickname = request.getHeader(NICKNAME);
        UserContextHolder.setNickname(StringUtils.isNotBlank(nickname) ? nickname : DEFAULT_NICKNAME);
        String userId = request.getHeader(USER_ID);
        UserContextHolder.setUserId(StringUtils.isNotBlank(userId) ? Long.parseLong(userId) : DEFAULT_USER_ID);
        String tenantId = request.getHeader(TENANT_ID);
        UserContextHolder.setTenantId(StringUtils.isNotBlank(tenantId) ? Long.parseLong(tenantId) : DEFAULT_TENANT_ID);
        String deptId = request.getHeader(DEPT_ID);
        UserContextHolder.setDeptId(StringUtils.isNotBlank(deptId) ? Long.parseLong(deptId) : DEFAULT_DEPT_ID);
        String postId = request.getHeader(POST_ID);
        UserContextHolder.setPostId(StringUtils.isNotBlank(postId) ? Long.parseLong(postId) : DEFAULT_POST_ID);
        String isAdminStr = request.getHeader("isAdmin");
        Boolean isAdmin = StringUtils.isNotBlank(isAdminStr) ? Boolean.parseBoolean(isAdminStr) : false;
        if (UserContextHolder.getUserId() == 1L || isAdmin) {
            UserContextHolder.setIsAdmin(true);
        } else {
            UserContextHolder.setIsAdmin(false);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.close();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
