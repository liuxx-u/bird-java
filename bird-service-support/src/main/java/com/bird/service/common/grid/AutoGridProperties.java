package com.bird.service.common.grid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @since 2021/1/18
 */
@Data
@ConfigurationProperties(prefix = "bird.service.grid")
public class AutoGridProperties {

    /**
     * 扫描的包路径
     */
    private String[] basePackages;

    /**
     * 是否对响应结果进行全局包装
     */
    private Boolean resultWrapper = true;
    /**
     * 是否自动注入审计字段值
     */
    private Boolean auditMetaObject = true;
}
