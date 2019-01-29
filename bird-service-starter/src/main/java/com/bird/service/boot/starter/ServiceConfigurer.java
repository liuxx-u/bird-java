package com.bird.service.boot.starter;

import com.bird.core.BirdConfigurer;
import com.bird.dubbo.gateway.route.RouteInitializePipe;
import com.bird.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liuxx
 * @date 2018/3/29
 */
@Configuration
@Import(BirdConfigurer.class)
public class ServiceConfigurer {

    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }

    @Bean
    public RouteInitializePipe routeInitializePipe(){
        return new RouteInitializePipe();
    }


}
