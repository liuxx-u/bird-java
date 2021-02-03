package com.bird.service.common.datarule;

import com.bird.service.common.service.query.FilterGroup;

/**
 * @author liuxx
 * @date 2018/9/30
 */
public class NullDataRuleProvider implements IDataRuleProvider {

    @Override
    public FilterGroup filter() {
        return null;
    }
}
