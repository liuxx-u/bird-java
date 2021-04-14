package com.bird.trace.logstash;

import ch.qos.logback.classic.Logger;
import com.bird.core.trace.TraceDefinition;
import com.bird.core.trace.dispatch.ITraceLogDispatcher;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author liuxx
 * @since 2021/4/13
 */
public class LogstashTraceLogDispatcher implements ITraceLogDispatcher {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(LogstashTraceLogDispatcher.class);

    @Override
    public void dispatch(Collection<TraceDefinition> traceLogs) {
        traceLogs.forEach(item -> LOGGER.callAppenders(new TraceLogstashProvider.TraceLoggingEvent(item, LOGGER)));
    }
}
