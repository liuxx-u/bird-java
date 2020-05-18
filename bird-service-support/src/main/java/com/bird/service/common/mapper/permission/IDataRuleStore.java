package com.bird.service.common.mapper.permission;

import com.bird.service.common.service.query.FilterGroup;

import java.util.Set;

/**
 * 规则元存储器
 *
 * @author liuxx
 * @date 2018/10/10
 */
public interface IDataRuleStore {

    /**
     * 存储规则元信息
     * @param rules 规则元信息
     */
    void store(Set<DataRuleInfo> rules);

    /**
     * 获取用户对指定表中的查询规则
     * @param userId 用户id
     * @param tables 表名
     * @return 查询规则
     */
    FilterGroup get(Long userId,String ...tables);
}
