package com.bird.web.file.upload.handler;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件处理器
 */
public interface IFileHandler {

    /**
     * 文件处理
     * @param file 文件信息
     * @return
     */
    void handle(MultipartFile file);
}
