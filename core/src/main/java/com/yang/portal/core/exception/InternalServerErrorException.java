package com.yang.portal.core.exception;


import com.yang.portal.core.result.CoreResultCode;

import java.util.Map;

public class InternalServerErrorException extends BaseException {

    public InternalServerErrorException() {
        super(CoreResultCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(Map<String, ?> errors) {
        super(CoreResultCode.INTERNAL_SERVER_ERROR, errors);
    }
}
