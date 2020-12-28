package com.bird.core.json;

/**
 * @author liuxx
 * @since 2020/12/28
 */
public interface JsonDeserializer {

    /**
     * 反序列化
     * @param json json串
     * @param <T> 反序列化类型
     * @return 反序列化的对象
     */
    <T> T deserialize(String json);
}
