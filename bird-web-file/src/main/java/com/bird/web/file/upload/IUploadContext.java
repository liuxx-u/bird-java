package com.bird.web.file.upload;

import org.springframework.web.multipart.MultipartFile;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 上传请求上下文
 *
 * @author liuxx
 * @date 2018/4/26
 */
public interface IUploadContext {

    /**
     * 获取 Content-Length
     * @return
     */
    long getContentLength();

    /**
     * 获取 schema
     * @return schema
     */
    String getSchema();

    /**
     * 获取 header
     * @param name 名称
     * @return
     */
    String getHeader(String name);

    /**
     * 获取参数值
     * @param name 名称
     * @return
     */
    String getParameter(String name);

    /**
     * 获取参数名集合
     * @return
     */
    Enumeration<String> getParameterNames();

    /**
     * 获取所有参数集合
     * @return
     */
    Map<String, String[]> getParameterMap();

    /**
     * 获取文件参数名集合
     * @return
     */
    Iterator<String> getFileNames();

    /**
     * 获取文件
     * @param name
     * @return
     */
    MultipartFile getFile(String name);

    /**
     * 获取文件集合
     * @param name
     * @return
     */
    List<MultipartFile> getFiles(String name);

    /**
     * 获取所有文件集合
     * @return
     */
    Map<String, MultipartFile> getFileMap();
}
