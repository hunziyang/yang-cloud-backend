package com.yang.portal.core.config.mvc;

import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.annotation.AnonymousController;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
        dateTimeFormatterRegistrar.setUseIsoFormat(true);
        dateTimeFormatterRegistrar.registerFormatters(registry);
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        // 使用 AnonymousController 注解的 Controller 默认增加前缀 /anonymous
        configurer.addPathPrefix(CoreConstant.ANONYMOUS, controller -> controller.isAnnotationPresent(AnonymousController.class));
    }
}
