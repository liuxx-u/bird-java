package com.bird.websocket.common.delay;

import com.bird.websocket.common.message.Message;

/**
 * 消息延时包装类
 *
 * @author YJ
 */
public class DelayMessage<T extends Message> {

    protected final T message;

    /** 预期过期时间 */
    private long expiredTime;
    /** 对象存活时长，小于等于0 表示永久存活 */
    private final long ttl;

    /**
     * 构造
     *
     * @param message 消息体
     * @param ttl     超时时长（单位为毫秒）
     */
    protected DelayMessage(T message, long ttl) {
        this.message = message;
        this.ttl = ttl;
        this.expiredTime = System.currentTimeMillis() + ttl;
    }

    /**
     * 判断是否过期
     *
     * @return 是否过期
     */
    boolean isExpired() {
        if (this.ttl > 0) {
            return expiredTime < System.currentTimeMillis();
        }
        return false;
    }

    /**
     * 获取值
     *
     * @return 值
     *
     * @since 4.0.10
     */
    public T get() {
        return this.message;
    }
}
