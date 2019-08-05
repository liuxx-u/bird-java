package com.bird.trace.client;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/8/4
 */
public class TraceContext {

    private static final ThreadLocal<ThreadTraceLog> THREAD_TRACE_LOG = ThreadLocal.withInitial(ThreadTraceLog::new);

    /**
     * 获取当前操作的日志记录
     * @return trace log
     */
    public TraceLog current() {
        ThreadTraceLog local = THREAD_TRACE_LOG.get();
        if (StringUtils.isBlank(local.currentKey) || !local.traceLogMap.containsKey(local.currentKey)) {
            return null;
        }
        return local.traceLogMap.get(local.currentKey);
    }

    /**
     * 添加子级跟踪日志
     * @param log log
     */
    public void push(TraceLog log){
        TraceLog current = current();
        if (current != null){
            log.setParentKey(current.getKey());
        }
    }



    @Data
    private static class ThreadTraceLog{

        /**
         * 当前的key
         */
        private String currentKey;
        /**
         * key-trace map
         */
        private Map<String,TraceLog> traceLogMap;

        private ThreadTraceLog(){
            traceLogMap = new HashMap<>();
        }
    }
}
