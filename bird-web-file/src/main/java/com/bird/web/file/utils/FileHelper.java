package com.bird.web.file.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author liuxx
 * @since 2020/5/18
 */
public class FileHelper {
    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getSuffix(String fileName) {
        return StringUtils.isBlank(fileName) ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return 文件后缀
     */
    public static String getSuffix(File file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getName();
        return getSuffix(fileName);
    }

    /**
     * 获取Content-Length
     * @param request 请求
     * @return content-length
     */
    public static long getContentLength(HttpServletRequest request){
        long size;
        try {
            size = Long.parseLong(request.getHeader(HttpHeaders.CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            size = request.getContentLengthLong();
        }
        return size;
    }
}
