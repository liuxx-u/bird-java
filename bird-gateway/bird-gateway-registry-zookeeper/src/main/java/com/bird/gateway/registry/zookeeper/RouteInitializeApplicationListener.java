package com.bird.gateway.registry.zookeeper;

import com.bird.gateway.common.IRouteRegistry;
import com.bird.gateway.common.IRouteScanner;
import com.bird.gateway.common.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/10
 */
public class RouteInitializeApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private List<IRouteScanner> scanners;
    private IRouteRegistry registry;

    @Value("${spring.application.name:}")
    private String application;

    public RouteInitializeApplicationListener(List<IRouteScanner> scanners,IRouteRegistry registry){
        this.scanners = scanners;
        this.registry = registry;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        scanners.forEach(p -> routeDefinitions.addAll(p.getRoutes()));
        this.registry.put(application,routeDefinitions);
    }
}