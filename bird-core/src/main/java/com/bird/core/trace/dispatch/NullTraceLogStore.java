package com.bird.core.trace.dispatch;

import com.bird.core.json.JsonSerializer;
import com.bird.core.trace.TraceDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class NullTraceLogStore implements ITraceLogStore {

    private final JsonSerializer jsonSerializer;

    public NullTraceLogStore(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    /**
     * 存储轨迹信息
     *
     * @param traceLogs 轨迹信息
     */
    @Override
    public void store(List<TraceDefinition> traceLogs) {
        for (TraceDefinition traceDefinition : traceLogs) {

            String content = "========================================== Trace ==========================================\n" +
                    "Entrance       : " + traceDefinition.getEntrance() + "\n" +
                    "Description    : " + traceDefinition.getDescription() + "\n" +
                    "User           : " + traceDefinition.getUserId() + "->" + traceDefinition.getUserName() + "\n" +
                    "time           : " + traceDefinition.getStartTime() + "->" + traceDefinition.getEndTime() + "\n" +
                    "TraceId        : " + traceDefinition.getParentTraceId() + "->" + traceDefinition.getTraceId() + "\n" +
                    "GlobalTraceId  : " + traceDefinition.getGlobalTraceId() + "\n" +
                    "Params         : " + this.jsonSerializer.serialize(traceDefinition.getParams()) + "\n" +
                    "ReturnValue    : " + this.jsonSerializer.serialize(traceDefinition.getReturnValue()) + "\n" +
                    "Claims         : " + this.jsonSerializer.serialize(traceDefinition.getClaims()) + "\n";

            log.trace(content);
        }
    }
}
