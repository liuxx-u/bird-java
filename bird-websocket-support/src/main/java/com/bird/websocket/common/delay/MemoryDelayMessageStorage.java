package com.bird.websocket.common.delay;

import cn.hutool.cache.GlobalPruneTimer;
import com.bird.websocket.common.message.Message;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @author YJ
 */
public class MemoryDelayMessageStorage implements IDelayMessageStorage {

    private Map<String, List<DelayMessage<Message>>> cacheMap;

    /** 读写锁 */
    private final StampedLock lock = new StampedLock();

    /** 正在执行的定时任务 */
    private ScheduledFuture<?> pruneJobFuture;

    /**
     * 默认 缓存失效时长， <code>0</code> 表示无限制，单位毫秒
     */
    protected long timeout;

    public MemoryDelayMessageStorage(long timeout) {
        this(timeout, Maps.newConcurrentMap());
    }

    /**
     * 构造
     *
     * @param timeout 过期时长
     * @param map     存储缓存对象的map
     */
    public MemoryDelayMessageStorage(long timeout, Map<String, List<DelayMessage<Message>>> map) {
        this.timeout = timeout;
        this.cacheMap = map;
    }

    @Override
    public boolean contain(String userId) {
        final long stamp = lock.readLock();
        try {
            List<DelayMessage<Message>> delayMessages = cacheMap.get(userId);
            if (delayMessages == null) {
                return false;
            }
            // 过期数据集
            for (DelayMessage<Message> delayMessage : delayMessages) {
                if (!delayMessage.isExpired()) {
                    return true;
                }
            }
        } finally {
            lock.unlockRead(stamp);
        }

        // 过期
        remove(userId);
        return false;
    }

    @Override
    public List<Message> get(String userId) {
        // 尝试读取缓存，使用乐观读锁
        long stamp = lock.readLock();
        try {
            List<DelayMessage<Message>> delayMessages = cacheMap.get(userId);
            if (delayMessages == null) {
                return Lists.newArrayList();
            }
            // 过期数据集
            List<DelayMessage<Message>> removedList = delayMessages.stream()
                    .filter(DelayMessage::isExpired)
                    .collect(Collectors.toList());
            delayMessages.removeAll(removedList);

            if (!delayMessages.isEmpty()) {
                return delayMessages.stream().map(DelayMessage::get).collect(Collectors.toList());
            }
        } finally {
            lock.unlock(stamp);
        }

        // 过期
        remove(userId);
        return Lists.newArrayList();
    }

    @Override
    public Message pop(String userId) {
        // 尝试读取缓存，使用乐观读锁
        long stamp = lock.readLock();
        try {
            List<DelayMessage<Message>> delayMessages = cacheMap.get(userId);
            if (delayMessages == null) {
                return null;
            }

            // 过期数据集
            List<DelayMessage<Message>> removedList = delayMessages.stream()
                    .filter(DelayMessage::isExpired)
                    .collect(Collectors.toList());
            delayMessages.removeAll(removedList);

            LinkedList<DelayMessage<Message>> delayMessageList = (LinkedList<DelayMessage<Message>>) delayMessages;
            if (!delayMessageList.isEmpty()) {
                return delayMessageList.pop().get();
            }
        } finally {
            lock.unlock(stamp);
        }

        // 过期
        remove(userId);
        return null;
    }

    @Override
    public void put(String userId, Message message, long timeout) {
        final long stamp = lock.writeLock();
        try {
            putWithoutLock(userId, message, timeout);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 加入元素，无锁
     *
     * @param userId  用户id
     * @param message 消息体
     * @param timeout 超时时长（单位为毫秒）
     */
    private void putWithoutLock(String userId, Message message, long timeout) {
        DelayMessage<Message> delayMessage = new DelayMessage<>(message, timeout);

        List<DelayMessage<Message>> delayMessages = cacheMap.get(userId);
        if (delayMessages == null) {
            delayMessages = Lists.newLinkedList();
        }
        ((LinkedList<DelayMessage<Message>>) delayMessages).addLast(delayMessage);

        cacheMap.put(userId, delayMessages);
    }

    /**
     * 移除key对应的对象
     *
     * @param userId 用户id
     */
    private void remove(String userId) {
        final long stamp = lock.writeLock();
        try {
            cacheMap.remove(userId);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 定时清理
     *
     * @param delay 间隔时长，单位毫秒
     */
    public void schedulePrune(long delay) {
        this.pruneJobFuture = GlobalPruneTimer.INSTANCE.schedule(this::prune, delay);
    }

    /**
     * 取消定时清理
     */
    public void cancelPruneSchedule() {
        if (null != pruneJobFuture) {
            pruneJobFuture.cancel(true);
        }
    }

    /**
     * 从缓存中清理过期对象，清理策略取决于具体实现
     */
    public final void prune() {
        final long stamp = lock.writeLock();
        try {
            pruneCache();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    protected void pruneCache() {
        Set<String> userIds = cacheMap.keySet();

        for (String userId : userIds) {
            List<DelayMessage<Message>> delayMessages = cacheMap.get(userId);
            if (CollectionUtils.isEmpty(delayMessages)) {
                cacheMap.remove(userId);
            }
            // 过期数据集
            List<DelayMessage<Message>> removedList = delayMessages.stream()
                    .filter(DelayMessage::isExpired)
                    .collect(Collectors.toList());
            delayMessages.removeAll(removedList);
            if (delayMessages.isEmpty()) {
                cacheMap.remove(userId);
            }
        }
    }
}
