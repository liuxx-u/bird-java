package com.bird.gateway.web.pipe.rpc.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 处理yyyy-MM-dd格式为yyyy-MM-dd HH:mm:ss
 * Dubbo泛化调用GenericFilter时间格式不支持yyyy-MM-dd
 * 转换类：org.apache.dubbo.common.utils.CompatibleTypeUtils.java
 *
 * @author liuxx
 * @date 2018/12/11
 */
@SuppressWarnings("all")
final class GenericJsonDeserializer {

    private static final String DATE_REG = "^[\\s]*[0-9]{4}-[01][0-9]-[0-3][0-9][\\s]*$";
    private static final String TIME_SUFFIX = " 00:00:00";

    static Object parse(String json) {

        Object obj = JSON.parse(json);
        if (obj == null) return null;
        else if (obj instanceof JSONObject) return parseObject(obj);
        else if (obj instanceof JSONArray) return parseArray(obj);
        else return obj;
    }

    private static Object parseArray(Object objArray) {
        JSONArray array = (JSONArray) objArray;
        return array.stream().map(GenericJsonDeserializer::parseObject).collect(Collectors.toList());
    }

    private static Object parseObject(Object obj) {
        JSONObject jsonObject = (JSONObject) obj;

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value == null) continue;

            if (value instanceof String) {
                String str = (String) value;
                if (StringUtils.isBlank(str)) continue;

                if (Pattern.matches(DATE_REG, str)) {
                    jsonObject.put(key, str.trim() + TIME_SUFFIX);
                }
            } else if (value instanceof JSONObject) {
                jsonObject.put(key, parseObject(value));
            }
        }

        return jsonObject;
    }
}
