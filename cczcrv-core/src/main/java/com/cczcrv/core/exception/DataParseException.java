package com.cczcrv.core.exception;

import com.cczcrv.core.HttpCode;

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

    protected HttpCode getHttpCode() {
        return HttpCode.INTERNAL_SERVER_ERROR;
    }

}
