package com.bird.web.file.upload.storage.disk;

/**
 * @author liuxx
 * @since 2020/10/21
 */
public interface IFileSuffixDirectoryMapping {

    /**
     * 获取文件格式对应的存储目录
     * @param suffix 文件格式
     * @return 存储目录
     */
    String getSuffixDirectory(String suffix);
}
