package com.bird.core.exception;

import com.bird.core.HttpCode;

/**
 *
 * @author liuxx
 * @date 2017/5/16
 */
public class DataParseException extends AbstractException {

    public DataParseException() {
    }

    public DataParseException(Throwable ex) {
        super(ex);
    }

    public DataParseException(String message) {
        super(message);
    }

    public DataParseException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public HttpCode getHttpCode() {
        return HttpCode.INTERNAL_SERVER_ERROR;
    }

}
