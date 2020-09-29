package com.bird.service.common.configuration;

import com.bird.service.common.trace.ColumnTraceProperties;
import com.bird.service.common.trace.IColumnTraceRecorder;
import com.bird.service.common.trace.ColumnTraceExchanger;
import com.bird.service.common.trace.interceptor.MybatisColumnTraceInterceptor;
import com.bird.service.common.trace.recorder.NoOpColumnTraceRecorder;
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
@ConditionalOnProperty(value = "bird.service.trace.column.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ColumnTraceProperties.class)
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MybatisColumnTraceInterceptor.class)
    public MybatisColumnTraceInterceptor columnTraceInterceptor(ColumnTraceProperties properties){
        return new MybatisColumnTraceInterceptor(properties);
    }


    @Bean
    @ConditionalOnMissingBean(IColumnTraceRecorder.class)
    public IColumnTraceRecorder columnTraceRecorder(){
        return new NoOpColumnTraceRecorder();
    }


    @Bean
    @ConditionalOnMissingBean(ColumnTraceExchanger.class)
    public ColumnTraceExchanger recordExchanger(ColumnTraceProperties properties,IColumnTraceRecorder recorder){
        return new ColumnTraceExchanger(properties,recorder);
    }
}
