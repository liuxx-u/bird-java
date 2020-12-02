package com.bird.lock.context;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前线程分布式锁信息
 *
 * @author liuxx
 * @since 2020/12/2
 */
@Slf4j
public class LockThreadContext {

    private static ThreadLocal<Map<String, LockValue>> LOCK_STATE_MAP = ThreadLocal.withInitial(HashMap::new);

    /**
     * 初始化
     *
     * @param lockKey 锁的Key
     */
    public static void initialize(String lockKey, String lockValue) {

        LockValue threadLockValue = new LockValue();
        threadLockValue.lockValue = lockValue;

        Map<String, LockValue> lockStateMap = lockStateMap();
        lockStateMap.put(lockKey, threadLockValue);
    }

    /**
     * 重入
     *
     * @param lockKey 锁的Key
     * @return 是否重入成功
     */
    public static boolean reentry(String lockKey) {
        LockValue lockValue = getLockValue(lockKey);
        if (lockValue == null) {
            return false;
        }

        lockValue.reentrantCount++;
        return true;
    }

    /**
     * 退出
     *
     * @param lockKey 锁的Key
     */
    public static void exit(String lockKey) {
        LockValue lockValue = getLockValue(lockKey);
        if (lockValue == null) {
            log.warn("当前线程:{}已经不持有该分布式锁：{}", Thread.currentThread().getName(), lockKey);
            return;
        }

        if (lockValue.reentrantCount <= 0) {
            lockStateMap().remove(lockKey);
        } else {
            lockValue.reentrantCount--;
        }
    }

    /**
     * 根据Key获取线程中持有的分布式锁信息
     * @param lockKey 锁的Key
     * @return 分布式锁信息
     */
    public static LockValue getLockValue(String lockKey){
        Map<String, LockValue> lockStateMap = lockStateMap();
        return lockStateMap.get(lockKey);
    }

    /**
     * 清除当前线程持有的分布式锁信息
     */
    public static void clear() {
        lockStateMap().clear();
    }

    private static Map<String, LockValue> lockStateMap() {
        return LOCK_STATE_MAP.get();
    }


    @Data
    public static class LockValue {

        /**
         * 锁的值
         */
        private String lockValue;

        /**
         * 重入次数
         */
        private int reentrantCount;
    }
}
