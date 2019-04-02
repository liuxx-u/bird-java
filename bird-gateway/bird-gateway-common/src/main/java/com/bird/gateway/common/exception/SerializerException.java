package com.bird.gateway.common.exception;

/**
 * @author liuxx
 * @date 2018/11/27
 */
public class SerializerException extends RuntimeException {

    private static final long serialVersionUID = 8068509879445395353L;

    public SerializerException(final Throwable e) {
        super(e);
    }

    public SerializerException(final String message) {
        super(message);
    }

    public SerializerException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}