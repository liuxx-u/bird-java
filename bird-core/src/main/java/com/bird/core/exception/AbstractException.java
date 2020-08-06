package com.bird.core.exception;

/**
 *
 * @author liuxx
 * @date 2017/5/16
 */
public abstract class AbstractException extends RuntimeException {

    protected String errorCode;

    public AbstractException() {
    }

    public AbstractException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AbstractException(String errorCode, String message, Throwable ex) {
        super(message, ex);
        this.errorCode = errorCode;
    }

    public AbstractException(IErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getDesc());
    }

    public AbstractException(IErrorCode errorCode, Throwable ex) {
        this(errorCode.getCode(), errorCode.getDesc(), ex);
    }

    /**
     * 获取异常对应的错误码
     *
     * @return 错误码
     */
    public String getErrorCode() {
        return this.errorCode;
    }
}