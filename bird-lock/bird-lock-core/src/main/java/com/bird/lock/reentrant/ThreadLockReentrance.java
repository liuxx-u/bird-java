package com.bird.lock.reentrant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2020/11/16
 */
public class ThreadLockReentrance implements ILockReentrance {

    private static ThreadLocal<Map<String, Integer>> LOCK_STATE_MAP = ThreadLocal.withInitial(HashMap::new);

    /**
     * 初始化
     *
     * @param lockKey 锁的Key
     */
    @Override
    public void initialize(String lockKey) {
        Map<String,Integer> lockStateMap = lockStateMap();
        lockStateMap.put(lockKey,0);
    }

    /**
     * 重入
     *
     * @param lockKey 锁的Key
     * @return 是否重入成功
     */
    @Override
    public boolean reentry(String lockKey) {
        Map<String, Integer> lockStateMap = lockStateMap();
        if (lockStateMap.containsKey(lockKey)) {
            lockStateMap.put(lockKey, lockStateMap.get(lockKey) + 1);
            return true;
        }
        return false;
    }

    /**
     * 退出
     *
     * @param lockKey 锁的Key
     */
    @Override
    public void exit(String lockKey) {
        Map<String, Integer> lockStateMap = lockStateMap();

        Integer state = lockStateMap.getOrDefault(lockKey, 0);
        state--;
        if (state <= 0) {
            this.remove(lockKey);
        } else {
            lockStateMap.put(lockKey, state);
        }
    }

    /**
     * 移除锁的重入信息
     *
     * @param lockKey 锁的Key
     */
    @Override
    public void remove(String lockKey) {
        lockStateMap().remove(lockKey);
    }

    /**
     * 清除所有锁的重入信息
     */
    @Override
    public void clear() {
        lockStateMap().clear();
    }

    private Map<String,Integer> lockStateMap(){
        return LOCK_STATE_MAP.get();
    }
}
