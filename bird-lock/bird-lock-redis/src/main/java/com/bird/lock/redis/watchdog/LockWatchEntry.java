package com.bird.lock.redis.watchdog;

import lombok.Data;

/**
 * @author liuxx
 * @date 2021/11/8
 */
@Data
public class LockWatchEntry {

    private String lockKey;

    private long keyExpire;

    private long threadId;

    private long lockedTime;

    private long lastRefreshTime;

    private long nextRefreshTime;

    private int renewCount;

    public LockWatchEntry(String lockKey, long threadId, long keyExpire) {
        this.lockKey = lockKey;
        this.threadId = threadId;
        this.keyExpire = keyExpire;
    }
}
