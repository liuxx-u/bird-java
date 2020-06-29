package com.bird.core.exception;

/**
 * @author liuxx
 * @since 2020/6/28
 */
public class UserArgumentException extends AbstractException {
    public UserArgumentException(){}

    public UserArgumentException(String message) {
        this(ErrorCode.A0001.getCode(), message);
    }

    public UserArgumentException(String errorCode, String message) {
        super(errorCode, message);
    }
}
