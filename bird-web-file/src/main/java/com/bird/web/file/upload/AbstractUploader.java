package com.bird.web.file.upload;

import com.bird.web.file.upload.handler.IFileHandler;
import com.bird.web.file.upload.resolver.IFileResolver;
import com.bird.web.file.upload.storage.IFileStorage;

/**
 * @author liuxx
 * @date 2018/4/25
 */
public abstract class AbstractUploader {

    /**
     * 文件解析器
     */
    protected IFileResolver resolver;
    /**
     * 文件处理器
     */
    protected IFileHandler handler;
    /**
     * 文件存储器
     */
    protected IFileStorage storage;

    /**
     * 文件上传
     * @param context
     * @return
     */
    protected UploadResult upload(IUploadContext context){



        return null;
    }
}
