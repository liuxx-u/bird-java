package com.bird.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author liuxx
 * @date 2017/6/27
 */
public final class DateHelper extends DateUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期转字符串
     * @param date 日期
     * @param pattern 模式
     * @return 时间字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 字符串转换为时间
     * @param strDate 时间字符串
     * @param pattern 格式
     * @return 时间
     */
    public static Date parse(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(strDate);
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 获取当前时间的字符串值
     * @param pattern 模式
     * @return 当前时间
     */
    public static String strNow(String pattern) {
        return format(new Date(),pattern);
    }

    /**
     * 比较两个时间相差多少秒
     *
     */
    public static long getDiffSecond(Date start, Date end) {
        return (end.getTime() - start.getTime()) / 1000;
    }

    /**
     * 比较两个时间相差多少分钟
     *
     */
    public static long getDiffMinute(Date start, Date end) {
        long diffSeconds = getDiffSecond(start, end);
        return diffSeconds/60;
    }

    /**
     * 比较两个时间相差多少天
     *
     */
    public static int getDiffDay(Date start, Date end) {
        start = getDayBegin(start);
        end = getDayBegin(end);
        if (start == null || end == null) return 0;

        long diffSeconds = getDiffSecond(start, end);
        long day = diffSeconds / 60 / 60 / 24;
        return (int) Math.floor(day);
    }

    /**
     * 获取两个时间相差月份
     *
     */
    public static int getDiffMonth(Date start, Date end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        return (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
                + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    /**
     * 获取一天开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        String format = DateFormatUtils.format(date, DATE_PATTERN);
        try {
            return parseDate(format.concat(" 00:00:00"), TIMESTAMP_PATTERN);
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 获取一天结束时间
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        String format = DateFormatUtils.format(date, DATE_PATTERN);
        try {
            return parseDate(format.concat(" 23:59:59"), TIMESTAMP_PATTERN);
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 返回传入时间月份的开始时间
     *
     */
    public static Date getMonthBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return getDayBegin(cal.getTime());
    }

    /**
     * 返回传入时间月份的的结束时间
     *
     */
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return getDayEnd(cal.getTime());
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
