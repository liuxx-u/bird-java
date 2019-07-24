package com.bird.service.common.service.query;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * 筛选条件组
 * @author liuxx
 * @date 2018/9/29
 */
@Getter
public class FilterGroup implements Serializable {
    /**
     * 组内筛选条件
     */
    private List<FilterRule> rules;

    /**
     * 连接符，支持and、or
     */
    private String operate;

    /**
     * 连接的组
     */
    private List<FilterGroup> groups;

    public FilterGroup(List<FilterRule> rules) {
        this.rules = rules;
    }

    public void and(List<FilterGroup> groups){
        this.operate = FilterOperate.AND.getValue();
        this.groups = groups;
    }

    public void or(List<FilterGroup> groups){
        this.operate = FilterOperate.OR.getValue();
        this.groups = groups;
    }
}
