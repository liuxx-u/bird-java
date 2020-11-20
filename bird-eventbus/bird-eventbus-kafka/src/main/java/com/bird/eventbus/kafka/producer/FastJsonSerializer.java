package com.bird.eventbus.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liuxx
 */
@Slf4j
public class FastJsonSerializer<T> implements Serializer<T> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, T data) {
        if(data == null){
            log.error("序列化的的事件为null");
        }

        String json = JSON.toJSONString(data);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
    }
}
