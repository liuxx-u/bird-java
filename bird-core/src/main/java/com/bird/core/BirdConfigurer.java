package com.bird.core;

import com.bird.core.cache.redis.RedisCacheConfigurer;
import com.bird.core.initialize.IInitializePipe;
import com.bird.core.initialize.InitializeExecutor;
import com.bird.core.utils.DozerHelper;
import com.bird.core.utils.SpringContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * @author liuxx
 * @date 2018/3/29
 */
@Configuration
@Import(RedisCacheConfigurer.class)
public class BirdConfigurer {

    @Value("${bird.basePackage:}")
    private String basePackage;

    @Bean
    @Lazy(false)
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean(initMethod = "initialize")
    public InitializeExecutor initializeExecutor(){
        return new InitializeExecutor(basePackage);
    }

    @Bean
    public DozerHelper dozerHelper() {
        return new DozerHelper();
    }
}
