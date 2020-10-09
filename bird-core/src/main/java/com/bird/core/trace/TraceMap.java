package com.bird.core.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
class TraceMap extends LinkedHashMap<String,TraceDefinition> {

    /**
     * 当前的trace id
     */
    private String currentTraceId;

    TraceDefinition current() {
        if (StringUtils.isEmpty(currentTraceId)) {
            return null;
        }
        return this.get(this.currentTraceId);
    }
}
