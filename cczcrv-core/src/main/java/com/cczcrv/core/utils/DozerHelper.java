package com.cczcrv.core.utils;

import com.beust.jcommander.internal.Lists;
import org.dozer.DozerBeanMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static jdk.nashorn.internal.objects.NativeDebug.map;

/**
 * Created by panda on 2017/6/14.
 */
public class DozerHelper  extends DozerBeanMapper {

    public <T> List<T> mapList(Collection sourceList, Class<T> destinationClass)
    {
        List destinationList = Lists.newArrayList();
        for (Iterator i$ = sourceList.iterator(); i$.hasNext(); ) { Object sourceObject = i$.next();
            Object destinationObject = map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }
    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 给指定定时间加上需要的天数
     * @param date
     * @param day
     * @return
     */
    public static String DateAddDayStr(Date date,int day){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar   calendar   =   new   GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return sf.format(calendar.getTime());
    }
    /**
     * 给指定定时间加上需要的天数
     * @param date
     * @param day
     * @return
     */
    public static Date DateAddDayDate(Date date,int day){
        Calendar   calendar   =   new   GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
    /**
     * 两个时间之间相差距离多少天
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }
}
