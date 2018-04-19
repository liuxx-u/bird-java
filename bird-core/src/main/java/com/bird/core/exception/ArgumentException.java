package com.bird.core.exception;

import com.bird.core.HttpCode;

/**
 *
 * @author liuxx
 * @date 2017/10/30
 */
public class ArgumentException extends AbstractException {

    public ArgumentException(String message) {
        super(message);
    }

    @Override
    public HttpCode getHttpCode() {
        return HttpCode.BAD_REQUEST;
    }
}
