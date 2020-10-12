package com.bird.service.common.trace;

import com.bird.core.trace.TraceContext;
import com.bird.service.common.trace.define.FieldTraceDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段轨迹 添加者
 *
 * 向当前的轨迹中添加字段变更记录
 *
 * @author liuxx
 * @since 2020/10/12
 */
public class FieldTraceAppender {

    private final static String CLAIM_KEY = "fields";

    private FieldTraceAppender() {
    }

    @SuppressWarnings("unchecked")
    public static void append(FieldTraceDefinition fieldTrace) {
        List<FieldTraceDefinition> fieldLogs = new ArrayList<>();

        Object exist = TraceContext.getClaim(CLAIM_KEY);
        if (exist instanceof List) {
            fieldLogs = (List<FieldTraceDefinition>) exist;
        }
        fieldLogs.add(fieldTrace);

        TraceContext.setClaim(CLAIM_KEY, fieldLogs);
    }
}
