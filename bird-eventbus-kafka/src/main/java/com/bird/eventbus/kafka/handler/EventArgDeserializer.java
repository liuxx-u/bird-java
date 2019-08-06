package com.bird.eventbus.kafka.handler;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.EventArg;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liuxx
 */
@Slf4j
public class EventArgDeserializer implements Deserializer<EventArg> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public EventArg deserialize(String topic, byte[] bytes) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        try {
            Class clazz = Class.forName(topic);
            if (EventArg.class.isAssignableFrom(clazz)) {
                return (EventArg) JSON.parseObject(json, clazz);
            }
        } catch (ClassNotFoundException e) {
            log.error("event:{}反序列化失败", topic, e);
        }
        return null;
    }

    @Override
    public void close() {

    }
}
