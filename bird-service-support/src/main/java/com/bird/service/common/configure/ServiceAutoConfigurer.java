package com.bird.service.common.configure;

import com.bird.service.common.mapper.injector.AuditMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
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
}
