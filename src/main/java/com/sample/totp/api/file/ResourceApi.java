package com.sample.totp.api.file;

import com.sample.totp.core.data.ApiData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件资源模块API接口
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Tag(name = "文件资源模块API")
public interface ResourceApi {

    @Operation(summary = "上传资源（限制100M）")
    ApiData<String> upload(@Parameter(description = "选择文件") MultipartFile selectFile) throws Exception;

    @Operation(summary = "预览资源")
    ResponseEntity<byte[]> view(@Parameter(description = "文件ID") String fileId) throws Exception;

    @Operation(summary = "下载资源")
    ResponseEntity<byte[]> download(@Parameter(description = "文件ID") String fileId) throws Exception;
}
