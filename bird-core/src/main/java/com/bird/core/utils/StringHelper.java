package com.bird.core.utils;

/**
 * Created by liuxx on 2017/5/18.
 */
public final class StringHelper extends org.apache.commons.lang3.StringUtils {
    public static boolean isNullOrEmpty(String str) {
        if (str == null) return true;
        if (str.length() == 0) return true;
        return false;
    }

    public static boolean isNullOrWhiteSpace(String str) {
        if (str == null) return true;
        if (str.trim().length() == 0) return true;
        return false;
    }

    public static String toCamelCase(String str) {
        if (isNullOrWhiteSpace(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toPascalCase(String str) {
        if (isNullOrWhiteSpace(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static String trim(String str, char c) {
        if(isNullOrWhiteSpace(str))return "";
        int len = str.length();
        int st = 0;
        char[] val = str.toCharArray();

        while ((st < len) && (val[st] <= c)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= c)) {
            len--;
        }
        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }

    public static String trimStart(String str, char c) {
        int len = str.length();
        int st = 0;
        char[] val = str.toCharArray();
        while ((st < len) && (val[st] <= c)) {
            st++;
        }
        return st > 0 ? str.substring(st, len) : str;
    }

    public static String trimEnd(String str,char c){
        int len = str.length();
        char[] val = str.toCharArray();

        while ((len>0) && (val[len - 1] <= c)) {
            len--;
        }
        return len < str.length() ? str.substring(0, len) : str;
    }
}
