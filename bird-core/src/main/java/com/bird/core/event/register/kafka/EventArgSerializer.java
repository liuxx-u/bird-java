package com.bird.core.event.register.kafka;

import com.bird.core.Check;
import com.bird.core.event.arg.EventArg;
import org.apache.kafka.common.serialization.Serializer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class EventArgSerializer implements Serializer<EventArg> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, EventArg eventArg) {
        Check.NotNull(eventArg, "eventArg");
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(eventArg);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Can't serialize object: " + eventArg, e);
        }
    }

    @Override
    public void close() {

    }
}
