package com.bird.service.cms.permission;

import com.bird.service.common.mapper.permission.IDataRuleProvider;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/9/29
 */
@Component
public class DataRuleProvider implements IDataRuleProvider {
    @Override
    public FilterGroup filter() {
        List<FilterRule> rules = new ArrayList<>();
        rules.add(new FilterRule("id", FilterOperate.IN,"3,59"));
        rules.add(new FilterRule("name",FilterOperate.CONTAINS,"2"));

        FilterGroup group = new FilterGroup(new ArrayList<>());
        FilterGroup group1 = new FilterGroup(rules);
        FilterGroup group2 = new FilterGroup(rules);

        group1.or(group2);
        group.and(group1);

        return group;
    }
}
