package com.bird.service.common.datarule;

import com.bird.service.common.datarule.DataRuleDefinition;
import com.bird.service.common.datarule.IDataRuleStore;
import com.bird.service.common.service.query.FilterGroup;

import java.util.Set;

/**
 * @author liuxx
 * @date 2018/10/10
 */
public class NullDataRuleStore implements IDataRuleStore {
    @Override
    public void store(Set<DataRuleDefinition> rules) {

    }

    @Override
    public FilterGroup get(String userId, String... tables) {
        return null;
    }
}
