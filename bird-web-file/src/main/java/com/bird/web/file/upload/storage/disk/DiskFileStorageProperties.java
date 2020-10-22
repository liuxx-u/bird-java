package com.bird.web.file.upload.storage.disk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/10/21
 */
@Data
@ConfigurationProperties(prefix = "bird.file.disk")
public class DiskFileStorageProperties {
    /**
     * URL前缀
     */
    private String urlPrefix;
    /**
     * 文件存储目录
     */
    private String uploadDir;
    /**
     * 指定存储目录请求头
     */
    private String uploadDirHeader = "bird-upload-dir";
}
