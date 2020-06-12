package com.bird.web.file.upload;

import com.bird.web.file.upload.base64.ServletBase64UploadContext;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 *
 * @author liuxx
 * @date 2018/4/26
 */
public class ServletUploader extends AbstractUploader {

    /**
     * multi-part 上传
     *
     * @param request 请求
     * @return 文件上传结果
     * @throws IOException exception
     */
    public UploadResult upload(HttpServletRequest request) throws IOException {
        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new UploadException("非文件上传请求");
        }

        IUploadContext uploadContext = new ServletUploadContext((MultipartHttpServletRequest) request);
        return super.upload(uploadContext);
    }

    /**
     * base64 文件流上传
     *
     * @param request 请求
     * @return 文件上传结果
     * @throws IOException exception
     */
    public UploadResult uploadBase64(HttpServletRequest request) throws IOException {
        IUploadContext uploadContext = new ServletBase64UploadContext(request);
        return super.upload(uploadContext);
    }
}
