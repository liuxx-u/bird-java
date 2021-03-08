package com.bird.websocket.common.delay;

import com.bird.websocket.common.message.Message;

import java.util.List;

/**
 * 延时消息存储器
 *
 * @author YJ
 */
public interface IDelayMessageStorage {

    /**
     * 给定的userId是否包含对应的延时消息
     *
     * @param userId 用户id
     * @return 判断结果
     */
    boolean contain(String userId);

    /**
     * 根据用户id获取所有可重新发送的消息集合
     *
     * @param userId 用户id
     * @return 消息集合
     */
    List<Message> get(String userId);

    /**
     * 根据用户id获取可重新发送等待时间最长的消息
     *
     * @param userId 用户id
     * @return 消息集合
     */
    Message pop(String userId);

    /**
     * 添加一个延时消息
     *
     * @param userId  用户id
     * @param message 消息
     * @param timeout 缓存有效期（单位为毫秒）
     */
    void put(String userId, Message message, long timeout);
}
