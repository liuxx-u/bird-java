package com.bird.trace.skywalking.dispatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bird.core.trace.TraceDefinition;
import com.bird.core.trace.dispatch.ITraceLogDispatcher;
import io.opentracing.Tracer;
import org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author liuxx
 * @since 2020/10/13
 */
public class SkywalkingTraceLogDispatcher implements ITraceLogDispatcher {

    private final static String TAG_DESCRIPTION = "描述";
    private final static String TAG_PARAM = "参数";
    private final static String TAG_USER_ID = "user.id";
    private final static String TAG_USER_NAME = "user.name";
    private final static String TAG_CLAIM_PREFIX = "claim.";

    /**
     * 发送跟踪日志
     *
     * @param traceLogs 跟踪日志
     */
    @Override
    public void dispatch(Collection<TraceDefinition> traceLogs) {
        if (CollectionUtils.isEmpty(traceLogs)) {
            return;
        }
        traceLogs.forEach(traceDefinition -> {
            Tracer tracer = new SkywalkingTracer();
            Tracer.SpanBuilder spanBuilder = tracer.buildSpan(traceDefinition.getEntrance())
                    .withStartTimestamp(traceDefinition.getStartTime())
                    .withTag(TAG_DESCRIPTION, traceDefinition.getDescription())
                    .withTag(TAG_PARAM, JSON.toJSONString(traceDefinition.getParams(), SerializerFeature.DisableCircularReferenceDetect))
                    .withTag(TAG_USER_ID, traceDefinition.getUserId())
                    .withTag(TAG_USER_NAME, traceDefinition.getUserName());


            for (Map.Entry<String, Object> entry : traceDefinition.getClaims().entrySet()) {
                spanBuilder.withTag(TAG_CLAIM_PREFIX + entry.getKey(), JSON.toJSONString(entry.getValue(), SerializerFeature.DisableCircularReferenceDetect));
            }

            spanBuilder.startManual().finish(traceDefinition.getEndTime());
        });
    }
}
