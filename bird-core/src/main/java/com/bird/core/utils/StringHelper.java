package com.bird.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuxx on 2017/5/18.
 */
public final class StringHelper extends org.apache.commons.lang3.StringUtils {

    public static String toCamelCase(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toPascalCase(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
