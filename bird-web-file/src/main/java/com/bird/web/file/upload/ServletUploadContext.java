package com.bird.web.file.upload;

import com.bird.web.file.utils.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Servlet上传请求上下文
 *
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
        return FileHelper.getContentLength(request);
    }

    /**
     * 获取 schema
     *
     * @return schema
     */
    @Override
    public String getSchema() {
        String schma = request.getHeader("x-forwarded-proto");
        if(StringUtils.isBlank(schma)){
            schma = request.getScheme();
        }
        return schma;
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
