package com.cczcrv.core.cache;

import com.cczcrv.core.cache.redis.RedisCacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuxx on 2017/5/16.
 */
@Configuration
public class CacheHelper {
    private static Logger logger = LoggerFactory.getLogger(CacheHelper.class);
    private static Cacher cacher;

    @Bean
    public Cacher setCache() {
        cacher = getCache();
        return cacher;
    }

    public static Cacher getCache() {
        if (cacher == null) {
            synchronized (CacheHelper.class) {
                if (cacher == null) {
                    cacher = new RedisCacher();
                }
            }
        }
        return cacher;
    }

    /** 获取锁 */
    public static boolean getLock(String key) {
        try {
            if (!getCache().exists(key)) {
                synchronized (CacheHelper.class) {
                    if (!getCache().exists(key)) {
                        if (getCache().setnx(key, String.valueOf(System.currentTimeMillis()))) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getLock", e);
        }
        int expires = 1000 * 60 * 3;
        String currentValue = (String) getCache().get(key);
        if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis() - expires) {
            if (getCache().setnx("UNLOCK_" + key, "0")) {
                unlock(key);
                getCache().set("UNLOCK_" + key, "0", 1);
            }
            return getLock(key);
        }
        return false;
    }

    public static void unlock(String key) {
        getCache().unlock(key);
    }
}
