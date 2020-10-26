package com.bird.lock.exception;

/**
 * @author liuxx
 * @since 2020/10/26
 */
public class DistributedLockException extends RuntimeException {

    public DistributedLockException() {
    }

    public DistributedLockException(String message) {
        super(message);
    }

    public DistributedLockException(String message, Throwable ex) {
        super(message, ex);
    }
}
