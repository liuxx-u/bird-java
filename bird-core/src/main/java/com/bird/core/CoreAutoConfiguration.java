package com.bird.core;

import com.bird.core.json.JsonDeserializer;
import com.bird.core.json.JsonSerializer;
import com.bird.core.json.NullJsonDeserializer;
import com.bird.core.json.NullJsonSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Configuration
public class CoreAutoConfiguration {

    /**
     * 注册 SpringContextHolder
     */
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    /**
     * 注册 空的JSON序列化器
     */
    @Bean
    @ConditionalOnMissingBean(JsonSerializer.class)
    public NullJsonSerializer nullJsonSerializer() {
        return new NullJsonSerializer();
    }

    /**
     * 注册 空的JSON反序列化器
     */
    @Bean
    @ConditionalOnMissingBean(JsonDeserializer.class)
    public NullJsonDeserializer nullJsonDeserializer() {
        return new NullJsonDeserializer();
    }
}
