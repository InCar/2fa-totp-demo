package com.sample.totp.core.data;

import org.springframework.http.HttpStatus;

/**
 * 请求API状态
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
public interface ApiCode {

    /**
     * 响应码：200-请求成功
     */
    int OK = HttpStatus.OK.value();

    int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

    int INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
