package com.yang.portal.core.utils;

import com.yang.portal.core.exception.InternalServerErrorException;

public class SqlOperateUtil {

    public static void OperateSuccess(int count){
        if(count != 1){
            throw new InternalServerErrorException(ErrorMapUtil.getErrorMap("操作失败"));
        }
    }
}
