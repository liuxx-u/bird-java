package com.bird.service.boot.starter.permission;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuxx
 * @date 2018/10/10
 */
@ConfigurationProperties(prefix = DataRuleConstant.PREFIX)
public class DataRuleProperties {
    private String basePackages;

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }
}
