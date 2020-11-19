package com.bird.eventbus.handler;

import java.util.List;

/**
 * 事件处理方法初始化监听器
 *
 * @author liuxx
 * @since 2020/11/19
 */
public interface IEventMethodInitializeListener {

    /**
     * 事件处理方法初始化 监听
     * @param definitions 事件处理方法集合
     */
    void initialize(List<EventMethodDefinition> definitions);
}
