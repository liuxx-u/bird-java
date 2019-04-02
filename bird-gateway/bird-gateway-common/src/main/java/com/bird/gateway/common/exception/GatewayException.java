package com.bird.gateway.common.exception;

/**
 * @author liuxx
 * @date 2018/11/28
 */
public class GatewayException extends RuntimeException {

    private static final long serialVersionUID = 8068509879445395353L;

    public GatewayException(final Throwable e) {
        super(e);
    }

    public GatewayException(final String message) {
        super(message);
    }

    public GatewayException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
