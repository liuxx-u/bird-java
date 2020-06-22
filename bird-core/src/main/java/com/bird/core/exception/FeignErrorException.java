package com.bird.core.exception;

/**
 * @author liuxx
 * @since 2020/6/22
 */
public class FeignErrorException extends AbstractException {

    private int httpCode;

    public FeignErrorException() {
    }

    public FeignErrorException(int httpCode, String errorCode, String message) {
        super(errorCode, message);
        this.httpCode = httpCode;
    }

    public FeignErrorException(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
