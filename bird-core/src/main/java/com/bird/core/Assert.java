package com.bird.core;

import com.bird.core.exception.UserArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuxx
 * @since 2020/6/28
 */
@Slf4j
@SuppressWarnings("all")
public class Assert {

    /**
     * 断言 - 不为NULL
     *
     * @param obj obj
     */
    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument is null.");
        }
    }

    /**
     * 断言 - 不为NULL
     *
     * @param obj     obj
     * @param message 错误消息
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 不为NULL
     *
     * @param obj       obj
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void notNull(Object obj, String errorCode, String message) {
        if (obj == null) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 为NULL
     *
     * @param obj obj
     */
    public static void isNull(Object obj) {
        if (obj != null) {
            throw new IllegalArgumentException("Argument is not null.");
        }
    }

    /**
     * 断言 - 为NULL
     *
     * @param obj     obj
     * @param message 错误消息
     */
    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 为NULL
     *
     * @param obj       obj
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void isNull(Object obj, String errorCode, String message) {
        if (obj != null) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 相等
     *
     * @param obj1 源对象
     * @param obj2 目标对象
     */
    public static void equal(Object obj1, Object obj2) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException("Arguments is not equal.");
        }
    }

    /**
     * 断言 - 相等
     *
     * @param obj1    源对象
     * @param obj2    目标对象
     * @param message 错误消息
     */
    public static void equal(Object obj1, Object obj2, String message) {
        if (!Objects.equals(obj1, obj2)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 相等
     *
     * @param obj1      源对象
     * @param obj2      目标对象
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void equals(Object obj1, Object obj2, String errorCode, String message) {
        if (!Objects.equals(obj1, obj2)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 不为空
     *
     * @param sequence 字符串
     */
    public static void notEmpty(CharSequence sequence) {
        if (StringUtils.isEmpty(sequence)) {
            throw new IllegalArgumentException("CharSequence is empty.");
        }
    }

    /**
     * 断言 - 不为空
     *
     * @param sequence 字符串
     * @param message  错误消息
     */
    public static void notEmpty(CharSequence sequence, String message) {
        if (StringUtils.isEmpty(sequence)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 不为空
     *
     * @param sequence  字符串
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void notEmpty(CharSequence sequence, String errorCode, String message) {
        if (StringUtils.isEmpty(sequence)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 不为空白字符
     *
     * @param sequence 字符串
     */
    public static void notBlank(CharSequence sequence) {
        if (StringUtils.isBlank(sequence)) {
            throw new IllegalArgumentException("CharSequence is blank.");
        }
    }

    /**
     * 断言 - 不为空白字符
     *
     * @param sequence 字符串
     * @param message  错误消息
     */
    public static void notBlank(CharSequence sequence, String message) {
        if (StringUtils.isBlank(sequence)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 不为空白字符
     *
     * @param sequence  字符串
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void notBlank(CharSequence sequence, String errorCode, String message) {
        if (StringUtils.isBlank(sequence)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 空白字符
     *
     * @param sequence 字符串
     */
    public static void isBlank(CharSequence sequence) {
        if (StringUtils.isNotBlank(sequence)) {
            throw new IllegalArgumentException("CharSequence is blank.");
        }
    }

    /**
     * 断言 - 空白字符
     *
     * @param sequence 字符串
     * @param message  错误消息
     */
    public static void isBlank(CharSequence sequence, String message) {
        if (StringUtils.isNotBlank(sequence)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 空白字符
     *
     * @param sequence  字符串
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void isBlank(CharSequence sequence, String errorCode, String message) {
        if (StringUtils.isNotBlank(sequence)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     */
    public static void contains(String text, String subText) {
        if (!StringUtils.contains(text, subText)) {
            throw new IllegalArgumentException("Not contains.");
        }
    }

    /**
     * 断言 - 包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     * @param message 错误消息
     */
    public static void contains(String text, String subText, String message) {
        if (!StringUtils.contains(text, subText)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     * @param message 错误消息
     */
    public static void contains(String text, String subText, String errorCode, String message) {
        if (!StringUtils.contains(text, subText)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 不包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     */
    public static void notContains(String text, String subText) {
        if (StringUtils.contains(text, subText)) {
            throw new IllegalArgumentException(text + " contains " + subText);
        }
    }

    /**
     * 断言 - 不包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     * @param message 错误消息
     */
    public static void notContains(String text, String subText, String message) {
        if (StringUtils.contains(text, subText)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 不包含字符串
     *
     * @param text    被搜索字符串
     * @param subText 子字符串
     * @param message 错误消息
     */
    public static void notContains(String text, String subText, String errorCode, String message) {
        if (StringUtils.contains(text, subText)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 集合不为空
     *
     * @param collection 集合
     */
    public static void notEmpty(Collection collection) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException("Collection is empty.");
        }
    }

    /**
     * 断言 - 集合不为空
     *
     * @param collection 集合
     * @param message    错误消息
     */
    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 集合不为空
     *
     * @param collection 集合
     * @param errorCode  错误码
     * @param message    错误消息
     */
    public static void notEmpty(Collection collection, String errorCode, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 集合为空
     *
     * @param collection 集合
     */
    public static void isEmpty(Collection collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException("Collection is not empty.");
        }
    }

    /**
     * 断言 - 集合为空
     *
     * @param collection 集合
     * @param message    错误消息
     */
    public static void isEmpty(Collection collection, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 集合为空
     *
     * @param collection 集合
     * @param errorCode  错误码
     * @param message    错误消息
     */
    public static void isEmpty(Collection collection, String errorCode, String message) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - Map不为空
     *
     * @param map map
     */
    public static void notEmpty(Map map) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException("Map is empty.");
        }
    }

    /**
     * 断言 - Map不为空
     *
     * @param map     map
     * @param message 错误消息
     */
    public static void notEmpty(Map map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 集合不为空
     *
     * @param map       集合
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void notEmpty(Map map, String errorCode, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - Map为空
     *
     * @param map map
     */
    public static void isEmpty(Map map) {
        if (MapUtils.isNotEmpty(map)) {
            throw new IllegalArgumentException("Map is empty.");
        }
    }

    /**
     * 断言 - Map为空
     *
     * @param map     map
     * @param message 错误消息
     */
    public static void isEmpty(Map map, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 集合为空
     *
     * @param map       集合
     * @param errorCode 错误码
     * @param message   错误消息
     */
    public static void isEmpty(Map map, String errorCode, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 必须为true
     *
     * @param expression 表达式
     */
    public static void isTrue(Boolean expression) {
        if (BooleanUtils.isNotTrue(expression)) {
            throw new IllegalArgumentException("Argument is not true.");
        }
    }

    /**
     * 断言 - 必须为true
     *
     * @param expression 表达式
     * @param message    错误消息
     */
    public static void isTrue(Boolean expression, String message) {
        if (BooleanUtils.isNotTrue(expression)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 必须为true
     *
     * @param expression 表达式
     * @param errorCode  错误码
     * @param message    错误消息
     */
    public static void isTrue(Boolean expression, String errorCode, String message) {
        if (BooleanUtils.isNotTrue(expression)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 必须为false
     *
     * @param expression 表达式
     */
    public static void isFalse(Boolean expression) {
        if (BooleanUtils.isFalse(expression)) {
            throw new IllegalArgumentException("Argument is not false.");
        }
    }

    /**
     * 断言 - 必须为false
     *
     * @param expression 表达式
     * @param message    错误消息
     */
    public static void isFalse(Boolean expression, String message) {
        if (BooleanUtils.isTrue(expression)) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 必须为false
     *
     * @param expression 表达式
     * @param errorCode  错误码
     * @param message    错误消息
     */
    public static void isFalse(Boolean expression, String errorCode, String message) {
        if (BooleanUtils.isTrue(expression)) {
            throw new UserArgumentException(errorCode, message);
        }
    }

    /**
     * 断言 - 大于
     *
     * @param value 值
     * @param target   目标值
     */
    public static void greaterThan(Number value, Number target) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble <= targetDouble) {
            throw new IllegalArgumentException(value + " must greater than " + target);
        }
    }

    /**
     * 断言 - 大于
     *
     * @param value 值
     * @param target   目标值
     * @param message    错误消息
     */
    public static void greaterThan(Number value, Number target, String message) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble <= targetDouble) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 大于等于
     *
     * @param value 值
     * @param target   目标值
     */
    public static void greaterOrEqual(Number value, Number target) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble < targetDouble) {
            throw new IllegalArgumentException(value + " must greater or equal " + target);
        }
    }

    /**
     * 断言 - 大于
     *
     * @param value 值
     * @param target   目标值
     * @param message    错误消息
     */
    public static void greaterOrEqual(Number value, Number target, String message) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble < targetDouble) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 小于
     *
     * @param value 值
     * @param target   目标值
     */
    public static void lessThan(Number value, Number target) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble >= targetDouble) {
            throw new IllegalArgumentException(value + " must less than" + target);
        }
    }

    /**
     * 断言 - 小于
     *
     * @param value 值
     * @param target   目标值
     * @param message    错误消息
     */
    public static void lessThan(Number value, Number target, String message) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble >= targetDouble) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 小于等于
     *
     * @param value 值
     * @param target   目标值
     */
    public static void lessOrEqual(Number value, Number target) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble > targetDouble) {
            throw new IllegalArgumentException(value + " must less than" + target);
        }
    }

    /**
     * 断言 - 小于等于
     *
     * @param value 值
     * @param target   目标值
     * @param message    错误消息
     */
    public static void lessOrEqual(Number value, Number target, String message) {
        notNull(value);
        notNull(target);
        double valueDouble = value.doubleValue();
        double targetDouble = target.doubleValue();
        if (valueDouble > targetDouble) {
            throw new UserArgumentException(message);
        }
    }

    /**
     * 断言 - 正数
     *
     * @param value 值
     */
    public static void positive(Number value) {
        greaterThan(value, 0);
    }

    /**
     * 断言 - 正数
     *
     * @param value 值
     * @param message    错误消息
     */
    public static void positive(Number value,String message) {
        greaterThan(value, 0, message);
    }

    /**
     * 断言 - 非正数
     *
     * @param value 值
     */
    public static void notPositive(Number value) {
        lessOrEqual(value, 0);
    }

    /**
     * 断言 - 非正数
     *
     * @param value 值
     * @param message    错误消息
     */
    public static void notPositive(Number value,String message) {
        lessOrEqual(value, 0, message);
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param value 值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     */
    public static void between(Number value, Number min, Number max) {
        notNull(value);
        notNull(min);
        notNull(max);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble < minDouble || valueDouble > maxDouble) {
            throw new IllegalArgumentException("Length must between " + min + " and " + max);
        }
    }

    /**
     * 检查值是否在指定范围内
     *
     * @param value   值
     * @param min     最小值（包含）
     * @param max     最大值（包含）
     * @param message 错误消息
     */
    public static void between(Number value, Number min, Number max, String message) {
        notNull(value, message);
        notNull(min, message);
        notNull(max, message);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble < minDouble || valueDouble > maxDouble) {
            throw new UserArgumentException(message);
        }
    }
}
