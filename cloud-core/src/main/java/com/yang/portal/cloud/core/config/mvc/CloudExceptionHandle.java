package com.yang.portal.cloud.core.config.mvc;

import com.yang.portal.cloud.core.exception.FeignErrorException;
import com.yang.portal.core.config.mvc.GlobalExceptionHandle;
import com.yang.portal.core.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CloudExceptionHandle extends GlobalExceptionHandle {

    @ExceptionHandler(FeignErrorException.class)
    public Result feignErrorException(FeignErrorException feignErrorException) {
        return baseException(feignErrorException);
    }

}
