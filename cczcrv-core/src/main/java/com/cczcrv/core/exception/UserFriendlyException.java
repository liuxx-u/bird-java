package com.cczcrv.core.exception;

import com.cczcrv.core.HttpCode;

/**
 * Created by liuxx on 2017/7/13.
 */
public class UserFriendlyException extends AbstractException {

    public UserFriendlyException(String message) {
        super(message);
    }

    @Override
    protected HttpCode getHttpCode() {
        return HttpCode.BAD_REQUEST;
    }
}
