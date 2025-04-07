package com.yang.portal.core.exception;


import com.yang.portal.core.result.ResultCodeBase;
import lombok.Getter;

import java.util.Map;

/**
 * 根异常类
 */
@Getter
public class BaseException extends RuntimeException {

    private ResultCodeBase resultCodeBase;

    /**
     * 异常详情
     */
    private Map<String, ?> errors;

    public BaseException(ResultCodeBase resultCodeBase) {
        super(resultCodeBase.getMsg());
        this.resultCodeBase = resultCodeBase;
    }

    public BaseException(ResultCodeBase resultCodeBase, Map<String, ?> errors) {
        super(resultCodeBase.getMsg());
        this.resultCodeBase = resultCodeBase;
        this.errors = errors;
    }
}
