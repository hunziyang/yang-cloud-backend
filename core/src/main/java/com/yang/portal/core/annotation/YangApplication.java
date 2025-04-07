package com.yang.portal.core.annotation;

import com.yang.portal.Yang;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.lang.annotation.*;

/**
 * 新的启动类，默认扫描 Yang.class 所在包及其包一下的内容，
 * Bean 默认命名规则为全类名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@SpringBootApplication(
        scanBasePackageClasses = Yang.class,
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public @interface YangApplication {
}
