package com.yang.portal.core.exception;


import com.yang.portal.core.result.CoreResultCode;

import java.util.Map;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(CoreResultCode.BAD_REQUEST);
    }

    public BadRequestException(Map<String, ?> errors) {
        super(CoreResultCode.BAD_REQUEST, errors);
    }
}
