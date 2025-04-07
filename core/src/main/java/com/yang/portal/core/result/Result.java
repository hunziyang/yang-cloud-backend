package com.yang.portal.core.result;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Result<T> {

    private Integer code;
    private String msg;
    private T body;
    private Map<String, ?> errors;

    public static Result success() {
        return Result.builder().code(CoreResultCode.SUCCESS.getCode()).msg(CoreResultCode.SUCCESS.getMsg()).build();
    }

    public static <E> Result success(E body) {
        return Result.builder().code(CoreResultCode.SUCCESS.getCode()).msg(CoreResultCode.SUCCESS.getMsg()).body(body).build();
    }

    public static Result error(ResultCodeBase resultCodeBase) {
        return Result.builder().code(resultCodeBase.getCode()).msg(resultCodeBase.getMsg()).build();
    }

    public static Result error(ResultCodeBase resultCodeBase, Map<String, ?> errors) {
        return Result.builder().code(resultCodeBase.getCode()).msg(resultCodeBase.getMsg()).errors(errors).build();
    }
}
