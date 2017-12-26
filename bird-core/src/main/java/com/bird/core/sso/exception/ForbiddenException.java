package com.bird.core.sso.exception;

import com.bird.core.HttpCode;
import com.bird.core.exception.AbstractException;

public class ForbiddenException extends AbstractException {
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    protected HttpCode getHttpCode() {
        return HttpCode.FORBIDDEN;
    }
}
