package com.bird.web.file.upload;

import com.bird.core.exception.AbstractException;

/**
 * 定义文件上传异常
 *
 * @author liuxx
 * @date 2018/4/25
 */
public class UploadException extends AbstractException {

    public UploadException(String message) {
        super(message);
    }
    /**
     * 获取异常对应的业务编码
     *
     * @return
     */
    @Override
    public Integer getCode() {
        return 500;
    }
}
