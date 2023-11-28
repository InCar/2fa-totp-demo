package com.sample.totp.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 请求API异常
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {

    @Schema(description = "请求状态码：200-请求成功")
    private Integer code;

    @Schema(description = "异常数据")
    private Object data;

    public ApiException(Integer code) {
        this.code = code;
    }

    public ApiException(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }
}
