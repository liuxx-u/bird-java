package com.bird.core.utils;

import java.math.BigDecimal;

/**
 * @author liuxx
 * @date 2018/4/10
 */
public class NumberHelper {

    public static final BigDecimal getSafety(BigDecimal value){
        if(value == null)return BigDecimal.ZERO;
        return value;
    }
}
