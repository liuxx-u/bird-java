package com.bird.service.common.mapper.permission;

import com.bird.service.common.service.query.FilterGroup;

/**
 * 数据规则提供器
 * @author liuxx
 * @date 2018/9/29
 */
public interface IDataRuleProvider {

    /**
     * 根据字段名筛选数据
     * @param key 标识
     * @return 筛选规则组
     */
    FilterGroup filter(String key);
}
