package com.bird.trace.client.configure;

import com.alibaba.druid.pool.DruidDataSource;
import com.bird.trace.client.TraceContext;
import com.bird.trace.client.aspect.ITraceLogCustomizer;
import com.bird.trace.client.aspect.TraceableAspect;
import com.bird.trace.client.dispatch.DefaultTraceLogDispatcher;
import com.bird.trace.client.dispatch.IDefaultTraceLogStore;
import com.bird.trace.client.dispatch.ITraceLogDispatcher;
import com.bird.trace.client.sql.druid.DruidDataSourcePostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/5
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
@ConditionalOnProperty(value = "bird.trace.client.enabled", matchIfMissing = true)
public class TraceClientAutoConfigurer {

    private final ApplicationContext applicationContext;
    private final List<ITraceLogCustomizer> logCustomizers;

    public TraceClientAutoConfigurer(ApplicationContext applicationContext, ObjectProvider<List<ITraceLogCustomizer>> logCustomizersProvider) {
        this.applicationContext = applicationContext;
        this.logCustomizers = logCustomizersProvider.getIfAvailable();
    }

    /**
     * 日志拦截切面
     *
     * @return 切面
     */
    @Bean
    public TraceableAspect traceableAspect(TraceProperties traceProperties) {
        TraceableAspect traceableAspect = new TraceableAspect();
        traceableAspect.setDefaultSQLTypes(traceProperties.getSqlTypes());
        traceableAspect.setLogCustomizers(logCustomizers);

        return traceableAspect;
    }

    @Bean
    @ConditionalOnClass(DruidDataSource.class)
    public DruidDataSourcePostProcessor druidDataSourcePostProcessor() {
        return new DruidDataSourcePostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean({ITraceLogDispatcher.class, IDefaultTraceLogStore.class})
    public IDefaultTraceLogStore defaultTraceLogStore() {
        return logs -> log.warn("未注入IDefaultTraceLogStore实例，丢弃跟踪日志信息");
    }


    @Bean
    @ConditionalOnMissingBean(ITraceLogDispatcher.class)
    public ITraceLogDispatcher traceLogDispatcher(IDefaultTraceLogStore defaultTraceLogStore) {
        return new DefaultTraceLogDispatcher(defaultTraceLogStore);
    }

    /**
     * 初始化跟踪日志发送器
     */
    @PostConstruct
    public void initTraceContext() {
        ITraceLogDispatcher logDispatcher = applicationContext.getBean(ITraceLogDispatcher.class);
        TraceContext.init(logDispatcher);
    }
}
