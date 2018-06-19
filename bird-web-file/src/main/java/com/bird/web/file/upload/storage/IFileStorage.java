package com.bird.web.file.upload.storage;

import com.bird.web.file.upload.IUploadContext;
import com.bird.web.file.upload.UploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件存储器
 *
 * @author liuxx
 * @date 2018/4/25
 */
public interface IFileStorage {

    /**
     * 文件保存
     *
     * @param file    上传的文件
     * @param bytes   处理后的文件数据
     * @param context 上下文信息
     * @return 上传成功后的URL地址
     */
    String save(MultipartFile file, byte[] bytes, IUploadContext context) throws UploadException, IOException;
}
