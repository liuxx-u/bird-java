package com.bird.web.file.upload;

import com.bird.core.exception.AbstractException;
import com.bird.core.exception.ErrorCode;

/**
 * 定义文件上传异常
 *
 * @author liuxx
 * @date 2018/4/25
 */
public class UploadException extends AbstractException {

    public UploadException(String message) {
        super(ErrorCode.A0001.getCode(), message);
    }
}
