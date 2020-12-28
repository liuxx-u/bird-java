package com.bird.core.json;

/**
 * @author liuxx
 * @since 2020/12/28
 */
public interface JsonSerializer {

    /**
     * JSON 序列化
     *
     * @param object 对象
     * @return json
     */
    String serialize(Object object);
}
