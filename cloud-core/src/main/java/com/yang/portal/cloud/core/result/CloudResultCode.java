package com.yang.portal.cloud.core.result;

import com.yang.portal.core.result.ResultCodeBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CloudResultCode implements ResultCodeBase {

    FEIGN_ERROR(601,"Feign 请求异常");

    private Integer code;
    private String msg;
}
