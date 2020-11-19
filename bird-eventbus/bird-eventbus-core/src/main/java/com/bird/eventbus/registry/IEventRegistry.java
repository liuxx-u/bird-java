package com.bird.eventbus.registry;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public interface IEventRegistry {

    /**
     * 添加Topic事件处理方法
     *
     * @param topic  topic
     * @param method 处理方法
     */
    void add(String topic, Method method);

    /**
     * 获取Topic事件处理方法
     *
     * @param topic topic
     * @return 事件处理方法集合
     */
    Set<Method> getTopicMethods(String topic);

    /**
     * 获取所有的Topic
     * 如果开启EventBus，且未处理任何的事件，返回默认topics
     * @return 所有的Topic
     */
    String[] getAllTopics();
}
