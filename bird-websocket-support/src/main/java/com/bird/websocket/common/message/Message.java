package com.bird.websocket.common.message;

import java.io.Serializable;

/**
 * 消息
 *
 * @author yuanjian
 */
public interface Message extends Serializable {

    String DELAY_USER_KEY = "delay_user";

    /**
     * 获取消息类型
     *
     * @return 消息类型枚举
     */
    MessageTypeEnum getType();

    /**
     * 是否异步发送
     *
     * @return true：异步发送，其他，同步发送
     * 默认为同步发送
     */
    boolean isAsync();

    /**
     * 获取消息体内容
     *
     * @return 消息体内容
     */
    String getContent();

    /**
     * 判断当前消息是否开启延时缓存发送功能
     *
     * @return 判断结果
     */
    boolean isDelay();

    /**
     * 获取当前消息自定义的的延时缓存时长
     * <p>
     * 其返回结果
     * 大于0：缓存一定时长，未被消费也会被删除
     * 等于0：使用系统默认的缓存时长
     * 小于0：永不过期，直至被消费掉才会被删除
     *
     * @return 时长
     */
    long getDelayDuration();

    /**
     * 获取自定义数据，接口回调中使用
     *
     * @param key 数据key
     * @return 业务key
     */
    String getItem(String key);
}
