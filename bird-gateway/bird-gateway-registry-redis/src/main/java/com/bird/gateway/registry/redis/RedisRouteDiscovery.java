package com.bird.gateway.registry.redis;

import com.bird.gateway.common.IRouteDiscovery;
import com.bird.gateway.common.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author liuxx
 * @date 2019/5/20
 */
public class RedisRouteDiscovery implements IRouteDiscovery {

    private final RedisTemplate<String, RouteDefinition> redisTemplate;

    public RedisRouteDiscovery(RedisTemplate<String, RouteDefinition> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据路径获取对应的路由信息
     *
     * @param path path
     * @return route
     */
    @Override
    public RouteDefinition get(String path) {
        if (StringUtils.isBlank(path))return null;

        String cacheKey = RedisPathConstant.getCacheKey(path);
        return this.redisTemplate.opsForValue().get(cacheKey);
    }
}
