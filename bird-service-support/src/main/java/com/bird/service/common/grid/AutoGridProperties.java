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


}
