package com.sample.totp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件路径工具类
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
public final class FilePathUtil {

    public static final String APPLICATION_PATH = new ApplicationHome(FilePathUtil.class).getDir().getPath();
    public static final String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    private FilePathUtil() {

    }

    public static String getAppDir() {
        return new ApplicationHome(FilePathUtil.class).getDir().getPath();
    }

    public static String getAppDir(String subDir) {
        File storageDirWithDate = new File(getAppDir(), subDir);
        if (!storageDirWithDate.exists()) {
            storageDirWithDate.mkdirs();
        }
        return storageDirWithDate.getAbsolutePath();
    }

    public static String getAppDateDir() {
        return getAppDir("resource"
                + File.separator
                + DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now()));
    }

    public static String getAppManualDir() {
        return getAppDir("manual");
    }

    public static String newTimeFilename(String filename) {
        return DateTimeFormatter.ofPattern("HHmmss-").format(LocalDateTime.now()) + filename;
    }

    public static InputStream getInputStream(String filePath) throws IOException {
        String classpath = "classpath:";
        if (StringUtils.isNotBlank(filePath)) {
            if (filePath.startsWith(classpath)) {
                return new ClassPathResource(filePath.replaceFirst(classpath, "")).getInputStream();
            } else {
                return new FileInputStream(ResourceUtils.getFile(filePath));
            }
        }
        return null;
    }
}
