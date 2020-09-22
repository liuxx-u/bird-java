package com.bird.service.common.configuration;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.bird.service.common.ServiceProperties;
import com.bird.service.common.incrementer.StringKeyGenerator;
import com.bird.service.common.mapper.injector.AuditMetaObjectHandler;
import com.bird.service.common.mapper.interceptor.OptimizeBlockAttackInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liuxx
 * @since 2020/5/19
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class ServiceAutoConfiguration {

    private final static String PREFIX = "bird.service.";

    private final ServiceProperties serviceProperties;

    public ServiceAutoConfiguration(ServiceProperties serviceProperties){
        this.serviceProperties = serviceProperties;
    }


    /**
     * 加载 审计字段（createTime,modifiedTime）自动填充处理器
     *
     * @return 自动填充处理器
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "audit-meta-object", havingValue = "true", matchIfMissing = true)
    public AuditMetaObjectHandler auditMetaObjectHandler() {
        return new AuditMetaObjectHandler();
    }

    /**
     * 加载 防止全表更新与删除插件
     *
     * @return 防止全表更新与删除插件
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "block-attack", havingValue = "true", matchIfMissing = true)
    public OptimizeBlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new OptimizeBlockAttackInnerInterceptor(this.serviceProperties.getGlobalLogicDeleteField());
    }

    /**
     * 加载 乐观锁插件
     *
     * @return 乐观锁插件
     */
    @Bean
    @ConditionalOnProperty(value = PREFIX + "optimistic-lock-check", havingValue = "true")
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    @ConditionalOnBean(InnerInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<List<InnerInterceptor>> interceptorProvider) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptorProvider.ifAvailable(innerInterceptors -> innerInterceptors.forEach(interceptor::addInnerInterceptor));
        return interceptor;
    }

    /**
     * 加载String主键生成器
     *
     * @return 主键生成器
     */
    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator stringKeyGenerator() {
        return new StringKeyGenerator();
    }
}
