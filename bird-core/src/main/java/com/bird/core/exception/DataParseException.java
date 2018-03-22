package com.bird.core.exception;

import com.bird.core.HttpCode;

/**
 * Created by liuxx on 2017/5/16.
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

    public HttpCode getHttpCode() {
        return HttpCode.INTERNAL_SERVER_ERROR;
    }

}
