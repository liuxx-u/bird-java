package com.cczcrv.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuxx on 2017/6/27.
 */
public final class DateHelper {
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return org.apache.commons.lang.StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }

    public static String getCurrentDateString(String pattern) {
        return getDateString(new Date(),pattern);
    }

    public static String getDateString(Date date, String pattern) {
        if (date == null) return null;
        if (StringHelper.isNullOrWhiteSpace(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        return formater.format(date);
    }
}
