package com.bird.web.common.trace;

import com.bird.core.trace.TraceTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2020/10/12
 */
@Data
@ConfigurationProperties("bird.trace.request")
public class RequestTraceProperties {

    /**
     * 请求跟踪类型
     */
    private TraceTypeEnum traceType;
}
