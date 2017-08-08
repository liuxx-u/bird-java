package com.cczcrv.core.exception;

import com.cczcrv.core.HttpCode;
import com.cczcrv.core.controller.OperationResult;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuxx on 2017/5/16.
 */
public abstract class AbstractException extends RuntimeException {
    public AbstractException() {
    }

    public AbstractException(Throwable ex) {
        super(ex);
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable ex) {
        super(message, ex);
    }

    public void handler(OperationResult result) {
        result.setHttpCode(getHttpCode().value());
        if (StringUtils.isNotBlank(getMessage())) {
            result.setMessage(getMessage());
        } else {
            result.setMessage(getHttpCode().msg());
        }
    }

    protected abstract HttpCode getHttpCode();
}