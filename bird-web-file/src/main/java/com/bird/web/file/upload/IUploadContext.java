package com.bird.web.file.upload;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public interface IUploadContext {

    /**
     * 获取请求的CharacterEncoding
     * @return
     */
    String getCharacterEncoding();

    /**
     * 获取请求的Content-Type
     * @return
     */
    String getContentType();

    /**
     *
     * @return
     */
    long getContentLength();

    /**
     * 获取请求输入流
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * 是否是MultipartContent
     * @return
     */
    boolean isMultipartContent();
}
