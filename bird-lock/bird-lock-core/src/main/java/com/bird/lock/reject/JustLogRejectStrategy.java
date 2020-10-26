package com.bird.lock.reject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxx
 * @since 2020/10/22
 */
@Slf4j
public class JustLogRejectStrategy<T> implements RejectStrategy<T> {

    public static final JustLogRejectStrategy INSTANCE = new JustLogRejectStrategy();

    private JustLogRejectStrategy() {

    }

    @Override
    public T reject(String lockKey) {
        log.warn("failed lock key: " + lockKey);
        return null;
    }
}
