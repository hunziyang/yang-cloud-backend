package com.yang.portal.currentUserInfo.annotation;


import com.yang.portal.currentUserInfo.config.WebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WebConfig.class)
public @interface EnableCurrentUserInfo {
}
