package com.bird.service.boot.starter;

import com.bird.core.BirdConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liuxx
 * @date 2018/3/29
 */
@Configuration
@Import(BirdConfigurer.class)
public class ServiceConfigurer {
}
