package com.bird.service.common.grid.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author liuxx
 * @date 2017/6/22
 */
@Getter
@RequiredArgsConstructor
public enum  FilterOperate {

    /**
     * and
     */
    AND("and","and"),
    /**
     * or
     */
    OR("or","or"),
    /**
     * =
     */
    EQUAL("equal","="),
    /**
     * !=
     */
    NOTEQUAL("notequal","!="),
    /**
     * in
     */
    IN("in","in"),
    /**
     * <
     */
    LESS("less","<"),
    /**
     * <=
     */
    LESS_OR_EQUAL("lessorequal","<="),
    /**
     * >
     */
    GREATER("greater",">"),
    /**
     * >=
     */
    GREATER_OR_EQUAL("greaterorequal",">="),
    /**
     * like
     */
    START_WITH("startswith","like"),
    /**
     * like
     */
    END_WITH("endswith","like"),
    /**
     * like
     */
    CONTAINS("contains","like");


    private final String value;

    private final String dbValue;

    private static final Map<String, FilterOperate> mappings = new HashMap<>(16);

    static {
        for (FilterOperate operate : values()) {
            mappings.put(operate.getValue(), operate);
        }
    }

    /**
     * 根据value获取FilterOperate
     * @param value value
     * @return FilterOperate
     */
    @Nullable
    public static FilterOperate resolve(@Nullable String value) {
        return (value != null ? mappings.get(value.toLowerCase()) : null);
    }

    /**
     * 根据value获取FilterOperate
     * @param value value
     * @param defaultOperate defaultOperate
     * @return FilterOperate
     */
    @Nullable
    public static FilterOperate resolveOrDefault(@Nullable String value, FilterOperate defaultOperate) {
        if (value == null) {
            return defaultOperate;
        }
        return mappings.getOrDefault(value.toLowerCase(), defaultOperate);
    }

    /**
     * 判断是否包含指定的value值
     * @param value value
     * @return true or false
     */
    public static Boolean containsValue(String value){
        if(StringUtils.isBlank(value)) {
            return false;
        }

        return mappings.keySet().contains(value);
    }
}
