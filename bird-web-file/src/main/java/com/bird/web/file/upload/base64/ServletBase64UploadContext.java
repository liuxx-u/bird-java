package com.bird.web.file.upload.base64;

import com.bird.web.file.upload.IUploadContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Servlet base64上传请求上下文
 *
 * @author liuxx
 * @date 2018/6/19
 */
public class ServletBase64UploadContext implements IUploadContext {
    private final HttpServletRequest request;
    private final Map<String,MultipartFile> fileMap;

    public ServletBase64UploadContext(@NonNull HttpServletRequest request) {
        this.request = request;

        Map<String, MultipartFile> fileMap = new HashMap<>();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            String value = request.getParameter(name);
            if (StringUtils.startsWith(value, "data:")) {
                fileMap.put(name, Base64MultipartFile.init(value));
            }
        }
        this.fileMap = fileMap;
    }


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
        return (Iterator<String>)this.fileMap.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultipartFile getFile(String name) {
        return this.fileMap.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MultipartFile> getFiles(String name) {
        return Arrays.asList(this.getFile(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, MultipartFile> getFileMap() {
        return this.fileMap;
    }
}
