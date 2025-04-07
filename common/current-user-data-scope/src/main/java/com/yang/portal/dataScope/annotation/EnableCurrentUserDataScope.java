package com.yang.portal.dataScope.annotation;

import com.yang.portal.dataScope.interceptor.DataScopeInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(DataScopeInterceptor.class)
public @interface EnableCurrentUserDataScope {
}
