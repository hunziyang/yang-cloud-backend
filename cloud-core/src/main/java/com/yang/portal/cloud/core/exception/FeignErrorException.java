package com.yang.portal.cloud.core.exception;


import com.yang.portal.cloud.core.result.CloudResultCode;
import com.yang.portal.core.exception.BaseException;

import java.util.Map;

public class FeignErrorException extends BaseException {

    public FeignErrorException() {
        super(CloudResultCode.FEIGN_ERROR);
    }

    public FeignErrorException(Map<String, ?> errors) {
        super(CloudResultCode.FEIGN_ERROR, errors);
    }
}
