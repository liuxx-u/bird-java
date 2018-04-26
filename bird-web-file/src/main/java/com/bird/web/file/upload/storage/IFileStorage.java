package com.bird.web.file.upload.storage;

import java.io.FileOutputStream;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件存储器
 */
public interface IFileStorage {

    /**
     * 文件流保存
     * @param stream
     */
    void save(FileOutputStream stream);
}
