package com.sample.totp.core.data;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * 请求API数据
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 * @param <T> 支持泛型
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Tag(name = "请求API数据")
public class ApiData<T> implements ApiCode {

    @Schema(description = "请求状态码：200-请求成功")
    private Integer code = OK;

    @Schema(description = "异常信息")
    private String errorMessage = HttpStatus.OK.getReasonPhrase();

    @Schema(description = "请求数据")
    private T data;

    public ApiData(T data) {
        this.data = data;
    }

    public static <T> ApiData<T> of(T data) {
        return new ApiData<>(data);
    }
}
