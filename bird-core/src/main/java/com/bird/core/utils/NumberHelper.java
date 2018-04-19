package com.bird.core.utils;


import org.apache.commons.lang3.math.NumberUtils;
import java.math.BigDecimal;

/**
 * @author liuxx
 * @date 2018/4/10
 */
public final class NumberHelper extends NumberUtils {

    public static final BigDecimal getSafety(BigDecimal value){
        if(value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }
}
