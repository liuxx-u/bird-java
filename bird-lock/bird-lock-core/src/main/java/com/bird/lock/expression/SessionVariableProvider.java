package com.bird.lock.expression;

import com.bird.core.session.SessionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/2/4
 */
public class SessionVariableProvider implements IKeyVariableProvider {

    private final static String SESSION_KEY = "session";

    /**
     * 默认注入的表达式参数
     *
     * @return 参数集合
     */
    @Override
    public Map<String, Object> variables() {
        Map<String,Object> map = new HashMap<>(2);
        map.put(SESSION_KEY, SessionContext.getSession());
        return map;
    }
}
