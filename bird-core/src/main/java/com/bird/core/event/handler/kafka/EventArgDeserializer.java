package com.bird.core.event.handler.kafka;

import com.bird.core.event.arg.EventArg;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class EventArgDeserializer implements Deserializer<EventArg> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public EventArg deserialize(String s, byte[] bytes) {
        EventArg obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (EventArg) ois.readObject();
            ois.close();
            bis.close();
        } catch (Exception e) {

        }
        return obj;
    }

    @Override
    public void close() {

    }
}
