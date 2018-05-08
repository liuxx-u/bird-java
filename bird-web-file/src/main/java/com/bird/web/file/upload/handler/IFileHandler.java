package com.bird.web.file.upload.handler;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件处理器
 */
public interface IFileHandler {

    /**
     * 文件处理
     * @param bytes 文件信息
     * @return 处理完成的文件信息
     */
    byte[] handle(byte[] bytes);
}
