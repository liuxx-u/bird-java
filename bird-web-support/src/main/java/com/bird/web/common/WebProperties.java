package com.bird.web.common;

import com.bird.web.common.cors.CorsProperties;
import com.bird.web.common.idempotency.IdempotencyProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author liuxx
 * @since 2020/8/31
 */
@Data
@ConfigurationProperties(prefix = "bird.web")
public class WebProperties {

    /**
     * 包路径
     */
    private String[] basePackages = {};
    /**
     * 是否开启全局异常处理
     */
    private Boolean globalExceptionEnable = true;
    /**
     * 是否开启全局结果包装器
     */
    private Boolean globalResultWrapper = true;
    /**
     * 幂等性配置
     */
    @NestedConfigurationProperty
    private IdempotencyProperties idempotency = new IdempotencyProperties();
    /**
     * Cors 跨域配置
     */
    @NestedConfigurationProperty
    private CorsProperties cors = new CorsProperties();
}
