package com.yang.portal.core.utils;

import com.yang.portal.core.CoreConstant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapUtil {

    public static Map<String, String> getErrorMap(String error) {
        return new HashMap<String, String>(){{
            put(CoreConstant.ERROR_KEY, error);
        }};
    }
}
