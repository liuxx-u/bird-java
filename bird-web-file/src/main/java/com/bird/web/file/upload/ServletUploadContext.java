package com.bird.web.file.upload;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public class ServletUploadContext implements IUploadContext {

    private final MultipartHttpServletRequest request;

    public ServletUploadContext(@NonNull MultipartHttpServletRequest request) {
        this.request = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getContentLength() {
        long size;
        try {
            size = Long.parseLong(request.getHeader(HttpHeaders.CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            size = request.getContentLengthLong();
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getFileNames() {
        return request.getFileNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultipartFile getFile(String name) {
        return request.getFile(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MultipartFile> getFiles(String name) {
        return request.getFiles(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, MultipartFile> getFileMap() {
        return request.getFileMap();
    }
}
