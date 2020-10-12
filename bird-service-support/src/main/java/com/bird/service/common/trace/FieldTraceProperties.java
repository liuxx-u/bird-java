package com.bird.service.common.trace;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shaojie
 */
@Data
@ConfigurationProperties("bird.trace.db-field")
public class FieldTraceProperties {

    /**
     * 是否启用数据库字段变更记录跟踪, 默认不启用
     */
    private boolean enabled;

}
