package com.sample.totp.api.file.impl;

import com.sample.totp.api.file.ResourceApi;
import com.sample.totp.core.data.ApiData;
import com.sample.totp.util.FilePathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 件资源模块API接口控制器
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/file/resource")
public class ResourceController implements ResourceApi {

    public static final String RESOURCE_SERVICE_IMPL_HASH_KEY = ResourceController.class.getSimpleName() + "TotpTest";

    final StringRedisTemplate stringRedisTemplate;

    private String getFilePath(String fileId) {
        if (StringUtils.isNotBlank(fileId)) {
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            return hashOperations.get(RESOURCE_SERVICE_IMPL_HASH_KEY, fileId);
        }
        return null;
    }

    private String storageFileWithCache(File uploadFile) {
        if (null != uploadFile && uploadFile.exists()) {
            String uuid = UUID.randomUUID().toString();
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            hashOperations.put(RESOURCE_SERVICE_IMPL_HASH_KEY, uuid, uploadFile.getAbsolutePath());
            log.info("storageFileWithCache -> uuid={}, uploadFilePath={}", uuid, uploadFile.getAbsolutePath());
            return uuid;
        }

        return null;
    }

    @Override
    @PostMapping(value = "/upload")
    public ApiData<String> upload(@RequestPart MultipartFile selectFile) throws Exception {
        File destFile = new File(FilePathUtil.getAppDateDir(), FilePathUtil.newTimeFilename(selectFile.getOriginalFilename()));
        selectFile.transferTo(destFile);
        log.info("upload -> destFile={}", destFile.getAbsolutePath());
        return ApiData.of(storageFileWithCache(destFile));
    }

    @Override
    @GetMapping(value = "/view/{fileId}")
    public ResponseEntity<byte[]> view(@PathVariable String fileId) throws Exception {
        // 获取文件存储信息
        String filePath = getFilePath(fileId);
        if (StringUtils.isNotBlank(filePath)) {
            // 传输信息
            File downloadFile = new File(filePath);
            String downloadFileName = downloadFile.getName().substring(7);
            byte[] downloadFileBytes = FileUtils.readFileToByteArray(downloadFile);

            // 传输文件流
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(downloadFileName)));
            log.info("view -> fileName={}, mediaType={}", downloadFileName, URLConnection.guessContentTypeFromName(downloadFileName));
            return new ResponseEntity<>(downloadFileBytes, headers, HttpStatus.OK);
        }
        return null;
    }

    @Override
    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable String fileId) throws Exception {
        // 获取文件存储信息
        String filePath = getFilePath(fileId);
        if (StringUtils.isNotBlank(filePath)) {
            // 传输信息
            File downloadFile = new File(filePath);
            String downloadFileName = downloadFile.getName().substring(7);
            byte[] downloadFileBytes = FileUtils.readFileToByteArray(downloadFile);

            // 传输文件流
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(downloadFileName)));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8));
            log.info("download -> fileName={}, mediaType={}", downloadFileName, URLConnection.guessContentTypeFromName(downloadFileName));
            return new ResponseEntity<>(downloadFileBytes, headers, HttpStatus.OK);
        }
        return null;
    }
}
