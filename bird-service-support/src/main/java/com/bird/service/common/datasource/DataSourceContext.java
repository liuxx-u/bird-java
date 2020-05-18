package com.bird.service.common.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2020/5/18
 */
public class DataSourceContext {

    private DataSourceContext(){}

    private static final ThreadLocal<String> LOCAL = ThreadLocal.withInitial(() -> StringUtils.EMPTY);

    public static void set(String value){
        LOCAL.set(value);
    }

    public static String get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
