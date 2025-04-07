package com.yang.portal.dataScope.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataScopeColumn {

    // 表别名
    String tableAlias() default "";

    // 表字段
    String column() default "";

    String dataScopeType() default "";
}
