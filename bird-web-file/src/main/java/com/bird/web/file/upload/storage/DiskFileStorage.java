package com.bird.web.file.upload.storage;

import java.io.FileOutputStream;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 文件存储器——磁盘
 */
public class DiskFileStorage implements IFileStorage {

    private String path;

    /**
     * 文件流保存
     *
     * @param stream
     */
    @Override
    public void save(FileOutputStream stream) {

    }
}
