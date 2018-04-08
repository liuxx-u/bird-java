package com.bird.core.cache;

import com.bird.core.Constant;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author liuxx
 * @date 2018/4/8
 */
public class CacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        Class<?> cls = target.getClass();
        String cachePrefix = Constant.Cache.ClassKeyMap.get(cls);
        if (StringUtils.isBlank(cachePrefix)) {
            CacheConfig cacheConfig = cls.getAnnotation(CacheConfig.class);
            if (cacheConfig == null || ArrayUtils.isEmpty(cacheConfig.cacheNames())) {
                cachePrefix = cls.getName();
            } else {
                cachePrefix = cacheConfig.cacheNames()[0];
            }
            Constant.Cache.ClassKeyMap.put(cls, cachePrefix);
        }

        String cacheName;
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        CachePut cachePut = method.getAnnotation(CachePut.class);
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);

        if (cacheable != null && ArrayUtils.isNotEmpty(cacheable.value())) {
            cacheName = cacheable.value()[0];
        } else if (cachePut != null && ArrayUtils.isNotEmpty(cachePut.value())) {
            cacheName = cachePut.value()[0];
        } else if (cacheEvict != null && ArrayUtils.isNotEmpty(cacheEvict.value())) {
            cacheName = cacheEvict.value()[0];
        } else {
            cacheName = method.getName();
        }

        StringBuilder sbKey = new StringBuilder(cachePrefix)
                .append(":").append(cacheName)
                .append(":").append(generateKey(params));

        return sbKey.toString();
    }

    private String generateKey(Object... params) {
        if (params.length == 0) {
            return "arg[0]";
        } else {
            if (params.length == 1) {
                Object param = params[0];
                if (param != null && !param.getClass().isArray()) {
                    return param.toString();
                }
            }

            StringBuilder sb = new StringBuilder("arg[");
            for (int i = 0, len = params.length; i < len; i++) {
                sb.append(params[i].hashCode());
                if (i < len - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
