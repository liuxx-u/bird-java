package com.bird.core.exception;

import com.bird.core.HttpCode;

/**
 *
 * @author liuxx
 * @date 2017/7/13
 */
public class UserFriendlyException extends AbstractException {

    public UserFriendlyException(String message) {
        super(message);
    }

    @Override
    public Integer getCode() {
        return HttpCode.BAD_REQUEST.value();
    }
}
