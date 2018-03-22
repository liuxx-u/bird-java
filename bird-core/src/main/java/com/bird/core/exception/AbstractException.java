package com.bird.core.exception;

import com.bird.core.HttpCode;

/**
 * Created by liuxx on 2017/5/16.
 */
public abstract class AbstractException extends RuntimeException {
    public AbstractException() {
    }

    public AbstractException(Throwable ex) {
        super(ex);
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable ex) {
        super(message, ex);
    }

    public abstract HttpCode getHttpCode();
}