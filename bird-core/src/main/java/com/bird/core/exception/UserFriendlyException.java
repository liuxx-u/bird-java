package com.bird.core.exception;

/**
 * 用户友好的异常，错误消息将传递至调用方
 *
 * @author liuxx
 * @date 2017/7/13
 */
public class UserFriendlyException extends AbstractException {

    public UserFriendlyException(String message) {
        this(ErrorCode.A0001.getCode(), message);
    }

    public UserFriendlyException(String errorCode, String message) {
        super(errorCode, message);
    }

    public UserFriendlyException(String errorCode, String message, Throwable ex) {
        super(errorCode, message, ex);
    }

    public UserFriendlyException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserFriendlyException(ErrorCode errorCode, Throwable ex) {
        super(errorCode, ex);
    }
}
