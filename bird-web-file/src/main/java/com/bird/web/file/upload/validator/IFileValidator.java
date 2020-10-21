package com.bird.web.file.upload.validator;

import org.springframework.web.multipart.MultipartFile;

/**
 * 定义 文件验证器
 *
 * @author liuxx
 * @date 2018/4/27
 */
public interface IFileValidator {

    /**
     * 验证文件是否合法
     * @param file file
     * @return 验证结果
     */
    ValidateResult validate(MultipartFile file);
}
