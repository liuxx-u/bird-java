package com.bird.service.common.configure;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.bird.service.common.incrementer.StringKeyGenerator;
import com.bird.service.common.mapper.injector.AuditMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/5/19
 */
@Slf4j
@Configuration
public class ServiceAutoConfigurer {

    /**
     * 注入 审计字段（createTime,modifiedTime）自动填充处理器
     * @return 自动填充处理器
     */
    @Bean
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
