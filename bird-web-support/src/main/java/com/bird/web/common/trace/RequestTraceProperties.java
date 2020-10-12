package com.bird.web.common.trace;

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
     * 是否启用请求参数跟踪, 默认不启用
     */
    private boolean enabled;
}
