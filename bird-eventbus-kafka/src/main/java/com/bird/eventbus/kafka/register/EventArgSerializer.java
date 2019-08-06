package com.bird.eventbus.kafka.register;

import com.alibaba.fastjson.JSON;
import com.bird.core.Check;
import com.bird.eventbus.arg.EventArg;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liuxx
 */
public class EventArgSerializer implements Serializer<EventArg> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, EventArg eventArg) {
        Check.NotNull(eventArg, "eventArg");

        String json = JSON.toJSONString(eventArg);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {

    }
}
