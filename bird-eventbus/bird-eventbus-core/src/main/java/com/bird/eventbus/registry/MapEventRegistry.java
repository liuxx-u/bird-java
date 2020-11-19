package com.bird.eventbus.registry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liuxx
 * @since 2020/11/19
 */
public class MapEventRegistry implements IEventRegistry {

    /**
     * topic与处理方法映射关系
     */
    private final static ConcurrentMap<String, Set<Method>> EVENT_HANDLER_CONTAINER = new ConcurrentHashMap<>();

    /**
     * 添加Topic事件处理方法
     *
     * @param topic  topic
     * @param method 处理方法
     */
    @Override
    public void add(String topic, Method method) {
        if (StringUtils.isBlank(topic) || method == null) {
            return;
        }

        Set<Method> eventHandlers = EVENT_HANDLER_CONTAINER.computeIfAbsent(topic, p -> new HashSet<>());
        eventHandlers.add(method);
        EVENT_HANDLER_CONTAINER.put(topic, eventHandlers);
    }

    /**
     * 获取Topic事件处理方法
     *
     * @param topic topic
     * @return 事件处理方法集合
     */
    @Override
    public Set<Method> getTopicMethods(String topic) {
        if (StringUtils.isBlank(topic)) {
            return new HashSet<>();
        }
        return EVENT_HANDLER_CONTAINER.getOrDefault(topic, new HashSet<>());
    }

    /**
     * 获取所有的Topic
     *
     * @return 所有的Topic
     */
    @Override
    public String[] getAllTopics() {
        Set<String> keys = EVENT_HANDLER_CONTAINER.keySet();
        if (CollectionUtils.isEmpty(keys)) {
            return new String[]{"none-topic"};
        }
        return keys.toArray(new String[0]);
    }
}
