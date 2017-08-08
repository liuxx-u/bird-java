package com.cczcrv.core.cache.redis.jedis;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuxx on 2017/5/16.
 */
public class JedisShardInfo extends redis.clients.jedis.JedisShardInfo {

    public JedisShardInfo(String host, int port) {
        super(host, port);
    }

    public void setPassword(String password) {
        if (StringUtils.isNotBlank(password)) {
            super.setPassword(password);
        }
    }
}
