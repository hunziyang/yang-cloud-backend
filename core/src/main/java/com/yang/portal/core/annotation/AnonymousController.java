package com.yang.portal.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * 此注解为鉴权使用
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping
public @interface AnonymousController {

    @AliasFor(annotation = RequestMapping.class , attribute = "value")
    String[] value() default {};
}
