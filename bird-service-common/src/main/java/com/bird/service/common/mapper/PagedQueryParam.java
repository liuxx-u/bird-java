package com.bird.service.common.mapper;

import com.bird.service.common.service.query.PagedListQueryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author liuxx
 * @date 2017/10/10
 */
public class PagedQueryParam implements Serializable {

    private static ConcurrentHashMap<String, QueryDescriptor> CLASS_QUERY_DESCRIPTOR_MAP = new ConcurrentHashMap<>();

    /**
     * 简单构造，适用与单表查询
     *
     * @param query  查询条件
     * @param tClass 对应DTO类
     */
    public PagedQueryParam(PagedListQueryDTO query, Class<?> tClass) {
        this(query, tClass, null, null);

        this.filterSoftDelete = true;
    }

    /**
     * 复杂构造，适用于多表查询
     *
     * @param query  查询条件
     * @param tClass 映射的Class
     * @param from   from语句
     * @param where  where语句
     */
    public PagedQueryParam(PagedListQueryDTO query, Class<?> tClass, String from, String where) {
        this.query = query;
        this.from = from;
        this.where = where;

        if (tClass != null) {
            this.queryDescriptor = CLASS_QUERY_DESCRIPTOR_MAP.computeIfAbsent(tClass.getName(), className -> QueryDescriptor.parseClass(tClass));
        }
    }

    /**
     * 查询表达式，包括分页、排序和筛选
     */
    private PagedListQueryDTO query;

    /**
     * 自定义的查询数据源，主要用于多表关联
     */
    private String from;

    /**
     * 自定义的where条件
     */
    private String where;

    /**
     * DTO对应的查询描述符
     */
    private QueryDescriptor queryDescriptor;

    /**
     * 是否过滤软删除的数据（简单构造-单表时默认为true）
     */
    private boolean filterSoftDelete;

    String getDbFieldName(String field){
        return this.queryDescriptor.getDbFieldName(field);
    }

    String getSelect() {
        return this.queryDescriptor == null ? "" : this.queryDescriptor.getSelect();
    }

    String getFrom() {
        if (StringUtils.isNotBlank(this.from)) return this.from;
        else if (this.queryDescriptor != null) return this.queryDescriptor.getFrom();
        else return null;
    }

    String getWhere() {
        if (this.queryDescriptor == null || this.query == null || CollectionUtils.isEmpty(this.query.getFilters()))
            return this.where;
        else {
            String queryWhere = this.queryDescriptor.formatRules(this.query.getFilters());
            if (StringUtils.isNotBlank(this.where)) {
                return this.where + " and " + queryWhere;
            } else {
                return queryWhere;
            }
        }
    }

    boolean isFilterSoftDelete() {
        return filterSoftDelete;
    }

    public PagedListQueryDTO getQuery() {
        return query;
    }
    public void setQuery(PagedListQueryDTO query) {
        this.query = query;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setFilterSoftDelete(boolean filterSoftDelete) {
        this.filterSoftDelete = filterSoftDelete;
    }
}
