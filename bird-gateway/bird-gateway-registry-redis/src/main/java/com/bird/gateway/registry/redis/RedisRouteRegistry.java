package com.bird.gateway.registry.redis;

import com.bird.gateway.common.IRouteDiscovery;
import com.bird.gateway.common.IRouteRegistry;
import com.bird.gateway.common.RouteDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/17
 */
public class RedisRouteRegistry implements IRouteRegistry{

    private static final String PATH_DELIMITER = "/";

    private final RedisTemplate<String, RouteDefinition> redisTemplate;

    public RedisRouteRegistry(RedisTemplate<String, RouteDefinition> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 注册模块的路由信息集合
     *
     * @param module           module
     * @param routeDefinitions route list
     */
    @Override
    public void put(String module, List<RouteDefinition> routeDefinitions) {
        if (CollectionUtils.isNotEmpty(routeDefinitions)) {
            for (RouteDefinition route : routeDefinitions) {
                if (route == null || StringUtils.isBlank(route.getPath())) continue;
                String cacheKey = RedisPathConstant.getCacheKey(PATH_DELIMITER + route.getModule() + route.getPath());
                this.redisTemplate.opsForValue().set(cacheKey, route);
            }
        }
    }
}
