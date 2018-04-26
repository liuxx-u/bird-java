package com.bird.web.file.upload;

import com.bird.web.common.utils.RequestHelper;
import org.apache.commons.fileupload.FileUploadBase;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public class ServletUploadContext implements IUploadContext {

    private final HttpServletRequest request;

    public ServletUploadContext(HttpServletRequest request){this.request = request;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCharacterEncoding() {
        return this.getCharacterEncoding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return this.getContentType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getContentLength() {
        long size;
        try {
            size = Long.parseLong(request.getHeader(FileUploadBase.CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            size = request.getContentLength();
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultipartContent() {
        return RequestHelper.isMultipartContent(request);
    }
}
