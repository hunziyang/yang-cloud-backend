package com.yang.portal.currentUserInfo.interpector;

import com.yang.portal.currentUserInfo.UserInfoConstant;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_NICKNAME = "DEFAULT-NICKNAME";
    private static final Long DEFAULT_USER_ID = -1L;
    private static final Long DEFAULT_TENANT_ID = -1L;
    private static final Long DEFAULT_DEPT_ID = -1L;
    private static final Long DEFAULT_POST_ID = -1L;
    private static final Boolean DEFAULT_IS_ADMIN = Boolean.FALSE;
    private static final String DEFAULT_TOKEN = "DEFAULT-TOKEN";
    private static final String DEFAULT_DATA_SCOPE = "MYSELF";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(UserInfoConstant.TOKEN);
        UserContextHolder.setToken(StringUtils.isNotBlank(token) ? token : DEFAULT_TOKEN);
        String nickname = request.getHeader(UserInfoConstant.NICKNAME);
        UserContextHolder.setNickname(StringUtils.isNotBlank(nickname) ? nickname : DEFAULT_NICKNAME);
        String userId = request.getHeader(UserInfoConstant.USER_ID);
        UserContextHolder.setUserId(StringUtils.isNotBlank(userId) ? Long.parseLong(userId) : DEFAULT_USER_ID);
        String tenantId = request.getHeader(UserInfoConstant.TENANT_ID);
        UserContextHolder.setTenantId(StringUtils.isNotBlank(tenantId) ? Long.parseLong(tenantId) : DEFAULT_TENANT_ID);
        String deptId = request.getHeader(UserInfoConstant.DEPT_ID);
        UserContextHolder.setDeptId(StringUtils.isNotBlank(deptId) ? Long.parseLong(deptId) : DEFAULT_DEPT_ID);
        String postId = request.getHeader(UserInfoConstant.POST_ID);
        UserContextHolder.setPostId(StringUtils.isNotBlank(postId) ? Long.parseLong(postId) : DEFAULT_POST_ID);
        String dataScope = request.getHeader(UserInfoConstant.DATA_SCOPE);
        UserContextHolder.setDataScope(StringUtils.isNotBlank(dataScope) ? dataScope : DEFAULT_DATA_SCOPE);
        String isAdminStr = request.getHeader(UserInfoConstant.IS_ADMIN);
        Boolean isAdmin = StringUtils.isNotBlank(isAdminStr) ? Boolean.parseBoolean(isAdminStr) : DEFAULT_IS_ADMIN;
        if (UserContextHolder.getUserId() == 1L || isAdmin) {
            UserContextHolder.setIsAdmin(true);
        } else {
            UserContextHolder.setIsAdmin(false);
        }
        String requestId = request.getHeader(UserInfoConstant.REQUEST_ID);
        UserContextHolder.setRequestId(StringUtils.isNotBlank(requestId) ? requestId : UUID.randomUUID().toString().replace("-", ""));
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
