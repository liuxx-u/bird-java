package com.bird.core.cache;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author liuxx
 * @date 2017/5/16
 */
public interface ICacher {
    Object get(final String key);

    void set(final String key, final Serializable value, int seconds);

    void set(final String key, final Serializable value);

    Boolean setIfAbsent(String key, Serializable value);

    Boolean exists(final String key);

    Boolean del(final String key);

    Boolean expire(final String key, final int seconds);

    Boolean expireAt(final String key, final long unixTime);
}
