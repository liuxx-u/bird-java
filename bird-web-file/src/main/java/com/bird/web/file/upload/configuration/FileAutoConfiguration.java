package com.bird.web.file.upload.configuration;

import com.bird.web.file.upload.IUploadListener;
import com.bird.web.file.upload.ServletUploader;
import com.bird.web.file.upload.handler.IFileHandler;
import com.bird.web.file.upload.storage.IFileStorage;
import com.bird.web.file.upload.validator.IFileValidator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/10/21
 */
@Configuration
public class FileAutoConfiguration {

    /**
     * 注入ServletUploader
     *
     * @return 默认的上传器
     */
    @Bean
    @ConditionalOnBean(IFileStorage.class)
    public ServletUploader servletUploader(IFileStorage fileStorage, ObjectProvider<IUploadListener> uploadListener, ObjectProvider<IFileValidator> fileValidator, ObjectProvider<List<IFileHandler>> fileHandlers) {
        ServletUploader uploader = new ServletUploader();
        uploader.setStorage(fileStorage);

        uploadListener.ifAvailable(uploader::setUploadListener);
        fileValidator.ifAvailable(uploader::setValidator);
        fileHandlers.ifAvailable(uploader::setFileHandlers);
        return uploader;
    }
}
