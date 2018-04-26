package com.bird.web.file.upload;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public class ServletUploader extends AbstractUploader {

    public UploadResult upload(HttpServletRequest request){
        IUploadContext uploadContext = new ServletUploadContext(request);
        return super.upload(uploadContext);
    }
}
