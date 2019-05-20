package com.bird.gateway.registry.redis;

/**
 * @author liuxx
 * @date 2019/5/20
 */
public final class RedisPathConstant {

    private static final String CACHE_PREFIX = "gateway:";

    /**
     * 根据路径获取缓存Key
     * @param path path
     * @return cache key
     */
    public static String getCacheKey(String path){
        return CACHE_PREFIX + path;
    }
}
