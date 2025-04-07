package com.yang.portal.core.config.mvc;

import com.yang.portal.core.CoreConstant;
import com.yang.portal.core.exception.BadRequestException;
import com.yang.portal.core.exception.BaseException;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.result.Result;
import com.yang.portal.core.result.CoreResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception.class)
    public Result exception(Exception exception) {
        log.warn(String.format(CoreConstant.REQUEST_ERROR_MESSAGE, exception.getMessage(), exception));
        Map<String, String> errors = new HashMap<>();
        errors.put(CoreConstant.ERROR_KEY, exception.getMessage());
        return baseException(new BaseException(CoreResultCode.INTERNAL_SERVER_ERROR, errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return baseException(new BaseException(CoreResultCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(BindException.class)
    public Result bindException(BindException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return baseException(new BaseException(CoreResultCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(BadRequestException.class)
    public Result badRequestException(BadRequestException badRequestException) {
        return baseException(badRequestException);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public Result internalServerErrorException(InternalServerErrorException internalServerErrorException) {
        return baseException(internalServerErrorException);
    }

    @ExceptionHandler(BaseException.class)
    public Result baseException(BaseException baseException) {
        if (ObjectUtils.isNotEmpty(baseException.getErrors()) && baseException.getErrors().containsKey(CoreConstant.ERROR_KEY)) {
            log.warn(String.format(CoreConstant.REQUEST_ERROR_MESSAGE, baseException.getErrors().get(CoreConstant.ERROR_KEY)), baseException);
        }
        return Result.error(baseException.getResultCodeBase(), baseException.getErrors());
    }
}
