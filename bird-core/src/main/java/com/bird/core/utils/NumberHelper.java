package com.bird.core.utils;


import org.apache.commons.lang3.math.NumberUtils;
import java.math.BigDecimal;

/**
 * @author liuxx
 * @date 2018/4/10
 */
public final class NumberHelper extends NumberUtils {

    /**
     * 是否是正数
     * @param number 数字
     * @return number is positive
     */
    public static boolean isPositive(Number number) {
        return greaterThan(number, 0);
    }

    /**
     * 是否不是正数
     * @param number 数字
     * @return number is not positive
     */
    public static boolean isNotPositive(Number number){
        return !isPositive(number);
    }

    /**
     * 是否大于
     *
     * @param number 数字
     * @param target 目标值
     * @return number is greater than target number
     */
    public static boolean greaterThan(Number number, Number target) {
        if (number == null) return false;
        return number.doubleValue() > target.doubleValue();
    }

    /**
     * 是否小于
     *
     * @param number 数字
     * @param target 目标值
     * @return number is less than target number
     */
    public static boolean lessThan(Number number, Number target) {
        if (number == null) return false;
        return number.doubleValue() < target.doubleValue();
    }

    /**
     * 获取BigDecimal,value为null时,返回BigDecimal.ZERO
     *
     * @param value value
     * @return value or 0
     */
    public static BigDecimal getSafety(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    /**
     * 获取Integer,value为null时，返回0
     *
     * @param value value
     * @return value or 0
     */
    public static Integer getSafety(Integer value){
        if(value == null){
            return 0;
        }
        return value;
    }

    /**
     * 获取Long,value为null时，返回0
     *
     * @param value value
     * @return value or 0
     */
    public static Long getSafety(Long value){
        if(value == null){
            return 0L;
        }
        return value;
    }
}
