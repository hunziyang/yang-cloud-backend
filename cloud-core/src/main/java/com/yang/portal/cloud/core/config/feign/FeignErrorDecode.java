package com.yang.portal.cloud.core.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yang.portal.cloud.core.exception.FeignErrorException;
import com.yang.portal.core.config.jackson.JacksonConfig;
import com.yang.portal.core.exception.InternalServerErrorException;
import com.yang.portal.core.result.Result;
import com.yang.portal.core.utils.ErrorMapUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

@Slf4j
public class FeignErrorDecode implements ErrorDecoder {

    private static final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

    @SneakyThrows
    @Override
    public Exception decode(String s, Response response) {
        try {
            Result result = objectMapper.readValue(response.body().asInputStream(), Result.class);
            if (ObjectUtils.isNotEmpty(result.getErrors())) {
                log.warn("Feign error message:{}", result.getErrors());
                String msg = objectMapper.writeValueAsString(result.getErrors());
                return new FeignErrorException(ErrorMapUtil.getErrorMap(msg));
            }
            return new InternalServerErrorException(ErrorMapUtil.getErrorMap("Feign error"));
        } catch (IOException e) {
            Exception exception = new Default().decode(s, response);
            log.warn("Feign decode error", exception);
            return new FeignErrorException(ErrorMapUtil.getErrorMap("Feign decode error"));
        }
    }
}
