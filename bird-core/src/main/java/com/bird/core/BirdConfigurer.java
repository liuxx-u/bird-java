package com.bird.core;

import com.bird.core.cache.redis.RedisCacheConfigurer;
import com.bird.core.utils.DozerHelper;
import com.bird.core.utils.SpringContextHolder;
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

    @Bean
    public DozerHelper dozerHelper() {
        return new DozerHelper();
    }

    @Bean
    @Lazy(false)
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
