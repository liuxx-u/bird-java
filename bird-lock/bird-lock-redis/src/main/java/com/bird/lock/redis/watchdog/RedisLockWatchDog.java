package com.bird.lock.redis.watchdog;

import com.bird.lock.redis.configuration.RedisLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author liuxx
 * @date 2021/11/8
 */
@Slf4j
public class RedisLockWatchDog {

    private static final ConcurrentHashMap<Long, Map<String, LockWatchEntry>> WATCH_MAP = new ConcurrentHashMap<>();

    private final String keyPrefix;
    private final Integer maxRenewCount;
    private final StringRedisTemplate redisTemplate;
    private final ScheduledExecutorService scheduledExecutorService;

    public RedisLockWatchDog(StringRedisTemplate redisTemplate, RedisLockProperties redisLockProperties) {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("bird-lock-redis-watchdog-")
                .daemon(true)
                .build();

        this.redisTemplate = redisTemplate;
        this.keyPrefix = redisLockProperties.getKeyPrefix();

        WatchdogProperties watchdogProperties = redisLockProperties.getWatchdog();
        this.maxRenewCount = watchdogProperties.getRenewCount();
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(watchdogProperties.getPoolSize(), threadFactory);
    }

    public synchronized void addWatch(long threadId, String lockKey, long keyExpire) {
        LockWatchEntry entry = new LockWatchEntry(lockKey, threadId, keyExpire);
        long refreshPeriod = keyExpire / 2;

        long current = System.currentTimeMillis();
        entry.setLockedTime(current);
        entry.setNextRefreshTime(current + refreshPeriod);

        Map<String, LockWatchEntry> threadWatchMap = WATCH_MAP.getOrDefault(threadId, new HashMap<>(8));
        if (threadWatchMap.containsKey(lockKey)) {
            log.warn("the lock key is already under monitoring,thread id:{}", threadId);
            return;
        }
        threadWatchMap.put(lockKey, entry);
        WATCH_MAP.put(threadId, threadWatchMap);

        // start watch
        this.scheduledExecutorService.schedule(() -> this.watchAndRenew(threadId, lockKey), refreshPeriod, TimeUnit.MILLISECONDS);
    }

    public synchronized void removeWatch(Long threadId, String lockKey) {
        Map<String, LockWatchEntry> threadWatchMap = WATCH_MAP.get(threadId);
        if (Objects.isNull(threadWatchMap)) {
            log.warn("thread is no longer in the monitor range,thread id:{}", threadId);
            return;
        }

        threadWatchMap.remove(lockKey);
        if (threadWatchMap.isEmpty()) {
            WATCH_MAP.remove(threadId);
        }
    }

    private void watchAndRenew(long threadId, String lockKey) {
        Map<String, LockWatchEntry> threadWatchMap = WATCH_MAP.get(threadId);
        if (Objects.isNull(threadWatchMap)) {
            return;
        }
        LockWatchEntry entry = threadWatchMap.get(lockKey);
        if (Objects.isNull(entry)) {
            return;
        }
        if (entry.getRenewCount() >= maxRenewCount) {
            return;
        }
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        ThreadInfo threadInfo = threadMxBean.getThreadInfo(threadId);
        if (threadInfo == null || threadInfo.getThreadState() == Thread.State.TERMINATED) {
            return;
        }
        long refreshPeriod = entry.getKeyExpire() / 2;

        long current = System.currentTimeMillis();
        entry.setLastRefreshTime(current);
        entry.setNextRefreshTime(current + refreshPeriod);
        entry.setRenewCount(entry.getRenewCount() + 1);

        String key = this.keyPrefix + lockKey;
        // TODO: redis脚本续期，判断lockValue的值
        if (BooleanUtils.isTrue(redisTemplate.expire(key, entry.getKeyExpire(), TimeUnit.MILLISECONDS))) {
            this.scheduledExecutorService.schedule(() -> this.watchAndRenew(threadId, lockKey), refreshPeriod, TimeUnit.MILLISECONDS);
        }
        log.info("redis key renew,key:{},thread id:{},count:{}", lockKey, entry.getThreadId(), entry.getRenewCount());
    }
}
