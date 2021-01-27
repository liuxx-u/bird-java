package com.bird.service.common.grid.executor.jdbc;

import com.bird.service.common.grid.query.FilterGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2021/1/27
 */
public interface IFilterGroupParser {

    /**
     * @param filterGroup  查询条件
     * @param fieldNameMap 字段映射关系
     * @return where 语句
     */
    String formatSQL(FilterGroup filterGroup, Map<String, String> fieldNameMap);

    default String formatSQL(FilterGroup filterGroup) {
        return this.formatSQL(filterGroup, new HashMap<>(0));
    }
}
