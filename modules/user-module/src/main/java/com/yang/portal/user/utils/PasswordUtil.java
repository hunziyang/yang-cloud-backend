package com.yang.portal.user.utils;

import org.apache.commons.codec.digest.Md5Crypt;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 用户密码盐生成类
 */
public class PasswordUtil {

    private static final String MD5_PREFIX = "$YANG$";

    public static String getSalt() {
        char[] chars = ("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz").toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            stringBuffer.append(aChar);
        }
        return stringBuffer.toString();
    }

    public static String getPassword(String password, String salt) {
        return Md5Crypt.md5Crypt(
                password.getBytes(StandardCharsets.UTF_8),
                String.format("%s%s", MD5_PREFIX, salt), MD5_PREFIX);
    }

}
