package com.bird.web.file.upload.resolver;

import com.bird.web.file.upload.IUploadContext;
import com.bird.web.file.upload.UploadRequest;
import org.apache.commons.fileupload.FileItem;

/**
 * @author liuxx
 * @date 2018/4/26
 *
 * 单文件上传请求解析器
 */
public class DefaultFileResolver implements IFileResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public UploadRequest resolve(IUploadContext context) {
        return null;
    }
}
