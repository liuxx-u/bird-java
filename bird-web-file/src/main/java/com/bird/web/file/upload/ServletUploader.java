package com.bird.web.file.upload;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public class ServletUploader extends AbstractUploader {

    public UploadResult upload(HttpServletRequest request) throws IOException{
        if(!(request instanceof MultipartHttpServletRequest)){
            throw new UploadException("非文件上传请求");
        }

        IUploadContext uploadContext = new ServletUploadContext((MultipartHttpServletRequest) request);
        return super.upload(uploadContext);
    }
}
