package com.bird.websocket.common.message;

/**
 * 消息
 *
 * @author yuanjian
 */
public interface Message {

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
     * 获取业务数据，接口回调中使用
     *
     * @param key 业务数据key
     * @return 业务key
     */
    String getBusinessItem(String key);
}
