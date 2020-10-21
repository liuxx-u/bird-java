package com.bird.web.file.upload.storage.disk;

import com.bird.web.file.upload.configuration.FileAutoConfiguration;
import com.bird.web.file.upload.storage.IFileStorage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/10/21
 */
@Configuration
@ConditionalOnMissingBean(IFileStorage.class)
@ConditionalOnProperty(value = "bird.file.disk.upload-dir")
@AutoConfigureBefore(FileAutoConfiguration.class)
@EnableConfigurationProperties(DiskFileStorageProperties.class)
public class DiskFileStorageAutoConfiguration {

    private final DiskFileStorageProperties diskFileStorageProperties;

    public DiskFileStorageAutoConfiguration(DiskFileStorageProperties diskFileStorageProperties) {
        this.diskFileStorageProperties = diskFileStorageProperties;
    }

    @Bean
    @ConditionalOnMissingBean(IFileSuffixDirectoryMapping.class)
    public IFileSuffixDirectoryMapping fileSuffixDirectoryMapping() {
        return new DefaultFileSuffixDirectoryMapping();
    }

    @Bean
    public IFileStorage fileStorage(IFileSuffixDirectoryMapping fileSuffixDirectoryMapping) {
        return new DiskFileStorage(diskFileStorageProperties, fileSuffixDirectoryMapping);
    }
}
