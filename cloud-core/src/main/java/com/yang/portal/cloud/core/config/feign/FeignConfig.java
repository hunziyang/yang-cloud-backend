package com.yang.portal.cloud.core.config.feign;

import com.yang.portal.currentUserInfo.UserInfoConstant;
import com.yang.portal.currentUserInfo.contextHolder.UserContextHolder;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

@Configuration
@EnableFeignClients
public class FeignConfig implements RequestInterceptor {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(2000, 5000);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecode();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(UserInfoConstant.NICKNAME, UserContextHolder.getNickname());
        requestTemplate.header(UserInfoConstant.USER_ID, String.valueOf(UserContextHolder.getUserId()));
        requestTemplate.header(UserInfoConstant.TENANT_ID, String.valueOf(UserContextHolder.getTenantId()));
        requestTemplate.header(UserInfoConstant.DEPT_ID, String.valueOf(UserContextHolder.getDeptId()));
        requestTemplate.header(UserInfoConstant.POST_ID, String.valueOf(UserContextHolder.getPostId()));
        requestTemplate.header(UserInfoConstant.IS_ADMIN, String.valueOf(UserContextHolder.getIsAdmin()));
        requestTemplate.header(UserInfoConstant.TOKEN, UserContextHolder.getToken());
        requestTemplate.header(UserInfoConstant.REQUEST_ID, UserContextHolder.getRequestId());
    }
}
