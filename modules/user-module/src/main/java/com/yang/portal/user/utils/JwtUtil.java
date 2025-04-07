package com.yang.portal.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yang.portal.user.property.UserProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private UserProperty userProperty;
    private static Long EXPIRE_TIME;
    private static String SECRET;

    @PostConstruct
    public void init() {
        EXPIRE_TIME = userProperty.getJwt().getExpireTime();
        SECRET = userProperty.getJwt().getSecret();
    }

    public static String sign(String username) {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        Date expireDate = new Date(now + EXPIRE_TIME * 1000);
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "hs256");
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT
                .create()
                .withHeader(header)
                .withSubject(username)
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }
}
