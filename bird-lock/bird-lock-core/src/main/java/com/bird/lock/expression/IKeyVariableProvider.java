package com.bird.lock.expression;

import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/4
 */
public interface IKeyVariableProvider {

    /**
     * 默认注入的表达式参数
     * @return 参数集合
     */
    Map<String,Object> variables();
}
