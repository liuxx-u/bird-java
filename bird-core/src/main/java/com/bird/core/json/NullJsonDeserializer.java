package com.bird.core.json;

/**
 * @author liuxx
 * @since 2020/12/28
 */
public class NullJsonDeserializer implements JsonDeserializer {
    /**
     * 反序列化
     *
     * @param json json串
     * @return 反序列化的对象
     */
    @Override
    public <T> T deserialize(String json) {
        return null;
    }
}
