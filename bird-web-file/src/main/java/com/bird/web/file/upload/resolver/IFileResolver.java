package com.bird.web.file.upload.resolver;

import com.bird.web.file.upload.IUploadContext;
import com.bird.web.file.upload.UploadRequest;
import org.apache.commons.fileupload.FileItem;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件上传请求解析器
 */
public interface IFileResolver {

    /**
     * 请求解析
     * @param context 请求上下文
     * @return 文件信息
     */
    UploadRequest resolve(IUploadContext context);
}
