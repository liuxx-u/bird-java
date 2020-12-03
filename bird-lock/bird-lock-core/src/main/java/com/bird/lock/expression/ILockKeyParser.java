package com.bird.lock.expression;

import java.lang.reflect.Parameter;

/**
 * @author liuxx
 * @since 2020/12/2
 */
public interface ILockKeyParser {

    /**
     * 解析分布式锁的Key
     * @param keyPattern key pattern
     * @param parameters 参数集合
     * @param args 参数值集合
     * @return Lock key
     */
    String parse(String keyPattern, Parameter[] parameters, Object[] args);
}
