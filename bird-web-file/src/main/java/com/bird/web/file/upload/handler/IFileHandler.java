package com.bird.web.file.upload.handler;


import com.bird.web.file.FileItem;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件处理器
 */
public interface IFileHandler {

    /**
     * 文件处理
     * @param files 文件信息
     * @return
     */
    List<FileItem> handle(List<FileItem> files);
}
