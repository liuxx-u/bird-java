package com.bird.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuxx on 2017/6/27.
 */
public final class DateHelper {

    /**
     * 字符串转日期
     * @param strDate 时间字符串
     * @param pattern 模式
     * @return
     * @throws ParseException
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return org.apache.commons.lang.StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }

    /**
     * 获取当前时间的字符串值
     * @param pattern 模式
     * @return
     */
    public static String strNow(String pattern) {
        return format(new Date(),pattern);
    }

    /**
     * 日期转字符串
     * @param date 日期
     * @param pattern 模式
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) return null;
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        return formater.format(date);
    }
}
