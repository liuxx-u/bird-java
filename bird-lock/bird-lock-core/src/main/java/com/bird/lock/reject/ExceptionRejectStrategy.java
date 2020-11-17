package com.bird.lock.reject;

import com.bird.lock.exception.DistributedLockException;

/**
 * @author liuxx
 * @since 2020/10/22
 */
public class ExceptionRejectStrategy<T> implements RejectStrategy<T>{

    private static final String DEFAULT_MESSAGE = "操作正在进行中, 请稍后重试";

    private String message;

    public ExceptionRejectStrategy() {
        this.message = DEFAULT_MESSAGE;
    }

    public ExceptionRejectStrategy(String message) {
        this.message = message;
    }

    @Override
    public T reject(String lockKey) {
        throw new DistributedLockException(message);
    }
}
