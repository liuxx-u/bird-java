package com.bird.core;

import com.bird.core.utils.DozerHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2018/3/29
 */
@Configuration
public class BirdConfigurer {

    @Bean
    public DozerHelper dozerHelper(){
        return new DozerHelper();
    }
}
