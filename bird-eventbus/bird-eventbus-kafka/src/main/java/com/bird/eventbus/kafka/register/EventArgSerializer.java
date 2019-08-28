package com.bird.eventbus.kafka.register;

import com.alibaba.fastjson.JSON;
import com.bird.eventbus.arg.EventArg;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liuxx
 */
@Slf4j
public class EventArgSerializer implements Serializer<EventArg> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, EventArg eventArg) {
        if(eventArg == null){
            log.error("发送的事件为null");
        }

        String json = JSON.toJSONString(eventArg);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {

    }
}
