package com.bird.core.json;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author liuxx
 * @since 2020/12/28
 */
public class NullJsonSerializer implements JsonSerializer {
    /**
     * JSON 序列化
     *
     * @param object 对象
     * @return json
     */
    @Override
    public String serialize(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        return object.toString();
    }
}
