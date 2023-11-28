package com.sample.totp.web.exception;

import com.sample.totp.core.data.ApiData;
import com.sample.totp.core.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求API异常处理器
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ApiException.class)
    public ApiData<String> handleApiException(ApiException e) {
        log.error("handleApiException", e);
        return new ApiData<String>()
                .setCode(e.getCode())
                .setErrorMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiData<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("handleMissingServletRequestParameterException", e);
        Map<String, String> tips = new HashMap<>(1);
        tips.put(e.getParameterName(), e.getMessage());
        String errorMessage = "request parameter exception";
        if (StringUtils.isNotBlank(e.getMessage())) {
            errorMessage = e.getMessage();
        }
        return new ApiData<>()
                .setCode(ApiData.BAD_REQUEST)
                .setErrorMessage(errorMessage)
                .setData(tips);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiData<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> tips = new HashMap<>(e.getBindingResult().getAllErrors().size());
        bindingResult.getFieldErrors().forEach(error -> {
            tips.put(error.getField(), error.getDefaultMessage());
        });
        String errorMessage = "method argument exception";
        if (!tips.isEmpty()) {
            errorMessage = tips.values().iterator().next();
        }
        return new ApiData<>()
                .setCode(ApiData.BAD_REQUEST)
                .setErrorMessage(errorMessage)
                .setData(tips);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiData<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("handleConstraintViolationException", e);
        Map<String, String> tips = new HashMap<>(e.getConstraintViolations().size());
        Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
        errors.forEach(error -> {
            tips.put(error.getPropertyPath().toString(), error.getMessage());
        });
        String errorMessage = "constraint violation exception";
        if (!errors.isEmpty()) {
            errorMessage = errors.iterator().next().getMessage();
        }
        return new ApiData<>()
                .setCode(ApiData.BAD_REQUEST)
                .setErrorMessage(errorMessage)
                .setData(tips);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ApiData<String> handleDefaultException(Exception e) {
        log.error("handleDefaultException", e);
        return new ApiData<String>()
                .setCode(ApiData.INTERNAL_SERVER_ERROR)
                .setErrorMessage(e.getMessage());
    }
}
