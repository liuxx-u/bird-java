package com.bird.eventbus.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bird.eventbus.arg.IEventArg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liuxx
 */
@Slf4j
public class FastJsonDeserializer<T> implements Deserializer<T> {

    private final String[] eventTopics;

    FastJsonDeserializer(String[] eventTopics) {
        this.eventTopics = eventTopics;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        try {
            if (ArrayUtils.contains(eventTopics, topic)) {
                Class clazz = Class.forName(topic);
                if (IEventArg.class.isAssignableFrom(clazz)) {
                    return (T) JSON.parseObject(json, clazz);
                }
            }

            return JSON.parseObject(json, new TypeReference<T>() {
            });
        } catch (Exception e) {
            log.error("event:{}反序列化失败", topic, e);
        }
        return null;
    }

    @Override
    public void close() {

    }
}
