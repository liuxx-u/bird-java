package com.bird.service.common.datarule;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据规则提供器基类
 *
 * @author liuxx
 * @date 2018/10/18
 */
public abstract class AbstractDataRuleProvider implements IDataRuleProvider {

    /**
     * 从线程中获取当前登录用户的票据信息
     *
     * @return session
     */
    protected BirdSession getSession() {
        return SessionContext.getSession();
    }

    /**
     * 从线程中获取当前登录用户的userId
     *
     * @return userId
     */
    protected String getUserId() {
        return SessionContext.getUserId();
    }

    /**
     * 返回空的结果
     *
     * @return id = -1 的查询规则
     */
    protected FilterGroup empty() {
        List<FilterRule> rules = new ArrayList<>();
        rules.add(new FilterRule("id", "-1"));

        return new FilterGroup(rules);
    }
}
