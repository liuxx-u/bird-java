package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author liuxx
 * @date 2017/6/23
 */
@Getter
@Setter
public class ListQuery extends AbstractBO {
    private String sortField;
    private int sortDirection;
    private List<FilterRule> filters;
    private List<String> sumFields;

    public ListQuery() {
        filters = new ArrayList<>();
        sumFields = new ArrayList<>();
    }

    /**
     * 重置查询条件
     *
     * @param filter 查询条件
     */
    public void resetFilter(FilterRule filter) {
        if (StringUtils.isAnyBlank(filter.getField(), filter.getValue())) {
            return;
        }
        List<FilterRule> filterRules = this.filters.stream().filter(p -> !Objects.equals(p.getField(), filter.getField())).collect(Collectors.toList());
        filterRules.add(filter);
        this.filters = filterRules;
    }

    /**
     * 重置查询条件
     *
     * @param field 查询字段
     * @param value 查询值
     */
    public void resetFilter(String field, String value) {
        this.resetFilter(new FilterRule(field, value));
    }

    /**
     * 添加查询条件
     *
     * @param filter 查询条件
     */
    public void addFilter(FilterRule filter) {
        if (StringUtils.isAnyBlank(filter.getField(), filter.getValue())) {
            return;
        }
        this.filters.add(filter);
    }

    /**
     * 添加查询条件
     *
     * @param field 查询字段
     * @param value 查询值
     */
    public void addFilter(String field, String value) {
        this.addFilter(new FilterRule(field, value));
    }

    /**
     * 添加汇总字段
     *
     * @param sumField 查询条件
     */
    public void addSumField(String sumField) {
        if (StringUtils.isBlank(sumField)) {
            return;
        }
        this.sumFields.add(sumField);
    }
}
