package com.bird.trace.client.configure;

import com.bird.trace.client.TraceConstant;
import com.bird.trace.client.sql.TraceSQLType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/5
 */
@Data
@ConfigurationProperties(prefix = TraceConstant.PREFIX)
public class TraceProperties {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 记录的SQL类型
     */
    private List<TraceSQLType> sqlTypes = new ArrayList<>();
}
