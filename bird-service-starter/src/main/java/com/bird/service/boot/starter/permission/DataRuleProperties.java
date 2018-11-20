package com.bird.service.boot.starter.permission;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2018/10/10
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DataRuleConstant.PREFIX)
public class DataRuleProperties {
    private String basePackages;
}
