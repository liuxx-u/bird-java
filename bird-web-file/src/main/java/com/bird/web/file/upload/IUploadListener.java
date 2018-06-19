package com.bird.web.file.upload;

import com.bird.web.file.upload.validator.ValidateResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传监听器
 *
 * @author liuxx
 * @date 2018/4/27
 *
 * 文件上传监听器
 */
public interface IUploadListener {

    /**
     * 在验证前执行
     * @param file 文件
     * @param context 上下文信息
     */
    void beforeValidate(MultipartFile file,IUploadContext context);

    /**
     * 在验证成功后执行
     * @param file 文件
     * @param context 上下文信息
     * @param result 验证结果
     */
    void afterValidate(MultipartFile file, IUploadContext context, ValidateResult result);

    /**
     * 在文件处理前执行
     * @param file 文件
     * @param context 上下文信息
     */
    void beforeHandle(MultipartFile file,IUploadContext context);

    /**
     * 在文件处理成功后执行
     * @param file 文件
     * @param context 上下文信息
     */
    void afterHandle(MultipartFile file,IUploadContext context);

    /**
     * 在文件存储前执行
     * @param file 文件
     * @param context 上下文信息
     */
    void beforeStorage(MultipartFile file,IUploadContext context);

    /**
     * 在文件存储成功后执行
     * @param file 文件
     * @param context 上下文信息
     */
    void afterStorage(MultipartFile file,IUploadContext context,UploadResult result);
}
