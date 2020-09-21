package com.bird.service.common.configuration;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.bird.service.common.ServiceProperties;
import com.bird.service.common.incrementer.StringKeyGenerator;
import com.bird.service.common.mapper.injector.AuditMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/5/19
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceAutoConfiguration {

    private final static String PREFIX = "bird.service.";

    /**
     * 注入 审计字段（createTime,modifiedTime）自动填充处理器
     * @return 自动填充处理器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "audit-meta-object", havingValue = "true", matchIfMissing = true)
    public AuditMetaObjectHandler auditMetaObjectHandler(){
        return new AuditMetaObjectHandler();
    }

    /**
     * 注入String主键生成器
     * @return 主键生成器
     */
    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator stringKeyGenerator(){
        return new StringKeyGenerator();
    }
}
