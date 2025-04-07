package com.yang.portal.user.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "user")
@Component
@RefreshScope
public class UserProperty {

    private Login login;
    private Jwt jwt;

    @Data
    public static class Login{
        private Long loginExpireTime;
        private Long tenantLoginExpireTime;
    }

    @Data
    public static class Jwt{
        private Long expireTime;
        private String secret;
    }
}
