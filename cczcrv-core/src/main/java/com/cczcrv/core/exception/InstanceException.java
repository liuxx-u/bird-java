package com.cczcrv.core.exception;

import com.cczcrv.core.HttpCode;

/**
 * Created by liuxx on 2017/5/16.
 */
public class InstanceException extends AbstractException {
    public InstanceException() {
        super();
    }

    public InstanceException(Throwable t) {
        super(t);
    }

    protected HttpCode getHttpCode() {
        return HttpCode.INTERNAL_SERVER_ERROR;
    }
}
