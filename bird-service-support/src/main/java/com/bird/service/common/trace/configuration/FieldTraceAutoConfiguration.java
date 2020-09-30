package com.bird.service.common.trace.configuration;

import com.bird.service.common.trace.FieldTraceProperties;
import com.bird.service.common.trace.IFieldTraceRecorder;
import com.bird.service.common.trace.FieldTraceExchanger;
import com.bird.service.common.trace.interceptor.MybatisFieldTraceInterceptor;
import com.bird.service.common.trace.recorder.NullFieldTraceRecorder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 跟踪相关自动化配置类
 * @author shaojie
 */
@Configuration
@ConditionalOnProperty(value = "bird.service.db-field-trace.enabled", havingValue = "true")
@EnableConfigurationProperties(FieldTraceProperties.class)
public class FieldTraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MybatisFieldTraceInterceptor.class)
    public MybatisFieldTraceInterceptor fieldTraceInterceptor(FieldTraceProperties properties){
        return new MybatisFieldTraceInterceptor(properties);
    }


    @Bean
    @ConditionalOnMissingBean(IFieldTraceRecorder.class)
    public IFieldTraceRecorder fieldTraceRecorder(){
        return new NullFieldTraceRecorder();
    }


    @Bean
    @ConditionalOnMissingBean(FieldTraceExchanger.class)
    public FieldTraceExchanger fieldTraceExchanger(FieldTraceProperties properties, IFieldTraceRecorder recorder){
        return new FieldTraceExchanger(properties,recorder);
    }
}
