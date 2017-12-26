package com.bird.core;

import com.bird.core.exception.ArgumentException;
import com.bird.core.utils.StringHelper;

/**
 * Created by liuxx on 2017/10/30.
 */
public final class Check {

    /**
     * 校验参数不能为空
     * @param object 参数
     * @param name 参数名
     */
    public static final void NotNull(Object object, String name) {
        if (object.equals(null)) {
            throw new ArgumentException(name + "can not be null.");
        }
    }

    /**
     * 校验字符串不能为空白
     * @param value 参数
     * @param name 参数名
     */
    public static final void NotEmpty(String value, String name){
        if(StringHelper.isNullOrWhiteSpace(value)){
            throw new ArgumentException(name + "can not be null.");
        }
    }

    /**
     * 校验参数必须大于某值
     * @param value 参数
     * @param target 目标值
     * @param name 参数名
     */
    public static final void GreaterThan(Long value, Long target, String name) {
        if (value <= target) {
            throw new ArgumentException(name + "must greater than" + target);
        }
    }

    /**
     * 校验参数必须小于某值
     * @param value 参数
     * @param target 目标值
     * @param name 参数名
     */
    public static final void LessThan(Long value,Long target,String name){
        if (value >= target) {
            throw new ArgumentException(name + "must less than" + target);
        }
    }
}
