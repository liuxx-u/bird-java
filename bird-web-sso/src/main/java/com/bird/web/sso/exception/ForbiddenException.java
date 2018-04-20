package com.bird.web.sso.exception;

import com.bird.core.HttpCode;
import com.bird.core.exception.AbstractException;

/**
 * 定义未授权异常类
 *
 * @author liuxx
 */
public class ForbiddenException extends AbstractException {
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpCode getHttpCode() {
        return HttpCode.FORBIDDEN;
    }
}
