package com.bird.service.common.mapper.permission;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterRule;
import org.apache.commons.lang3.StringUtils;

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
     * @return session
     */
    protected BirdSession getSession(){
        return SessionContext.getSession();
    }

    /**
     * 从线程中获取当前登录用户的userId
     *
     * @return userId
     */
    protected Long getUserId() {
        BirdSession session = getSession();
        if (session == null) return 0L;
        if (session.getUserId() == null || StringUtils.isBlank(session.getUserId().toString())) return 0L;
        return Long.valueOf(session.getUserId().toString());
    }

    /**
     * 返回空的结果
     * @return id = -1 的查询规则
     */
    protected FilterGroup empty(){
        List<FilterRule> rules = new ArrayList<>();
        rules.add(new FilterRule("id","-1"));

        return new FilterGroup(rules);
    }
}
