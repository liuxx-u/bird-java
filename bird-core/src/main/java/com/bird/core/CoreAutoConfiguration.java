package com.bird.core;

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
}
