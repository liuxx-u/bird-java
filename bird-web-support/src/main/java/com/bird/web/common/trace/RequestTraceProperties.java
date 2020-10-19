package com.bird.web.common.trace;

import com.bird.core.trace.TraceTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * @author liuxx
 * @since 2020/10/12
 */
@Data
@ConfigurationProperties("bird.trace.request")
public class RequestTraceProperties {

    private final static String DEFAULT_ALL_HEADERS = "all";

    /**
     * 请求跟踪类型
     */
    private TraceTypeEnum traceType;
    /**
     * 跟踪的uri
     */
    private String[] urlPatterns = {"/*"};
    /**
     * 跟踪的请求头，默认跟踪所有的请求头
     */
    private String[] traceHeaders = {DEFAULT_ALL_HEADERS};

    boolean traceAllHeaders() {
        return Objects.equals(this.traceHeaders.length, 1) && Objects.equals(this.traceHeaders[0], DEFAULT_ALL_HEADERS);
    }
}
