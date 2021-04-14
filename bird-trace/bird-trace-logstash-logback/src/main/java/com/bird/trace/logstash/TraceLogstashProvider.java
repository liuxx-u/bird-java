package com.bird.trace.logstash;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.alibaba.fastjson.JSON;
import com.bird.core.trace.TraceDefinition;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.composite.AbstractFieldJsonProvider;

import java.io.IOException;

/**
 * @author liuxx
 * @since 2021/4/13
 */
public class TraceLogstashProvider extends AbstractFieldJsonProvider<ILoggingEvent> {

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        if (event instanceof TraceLoggingEvent) {
            TraceDefinition definition = ((TraceLoggingEvent) event).definition;
            generator.writeStringField("userId", definition.getUserId());
            generator.writeStringField("userName", definition.getUserName());
            generator.writeStringField("globalTraceId", definition.getGlobalTraceId());
            generator.writeStringField("traceId", definition.getTraceId());
            generator.writeStringField("parentTraceId", definition.getParentTraceId());
            generator.writeStringField("description", definition.getDescription());
            generator.writeStringField("params", JSON.toJSONString(definition.getParams()));
            generator.writeStringField("returnValue", JSON.toJSONString(definition.getReturnValue()));
            generator.writeNumberField("startTime", definition.getStartTime());
            generator.writeNumberField("endTime", definition.getEndTime());
            generator.writeStringField("claims", JSON.toJSONString(definition.getClaims()));
        }
    }

    public static class TraceLoggingEvent extends LoggingEvent {

        private TraceDefinition definition;

        public TraceLoggingEvent(TraceDefinition definition, Logger logger) {
            super(null, logger, Level.INFO, definition.getEntrance(), null, null);
            this.definition = definition;
        }
    }
}
