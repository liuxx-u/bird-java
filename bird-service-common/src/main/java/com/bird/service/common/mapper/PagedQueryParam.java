package com.bird.service.common.mapper;

import com.bird.core.utils.SpringContextHolder;
import com.bird.service.common.mapper.permission.IDataRuleProvider;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterRule;
import com.bird.service.common.service.query.PagedListQueryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author liuxx
 * @date 2017/10/10
 */
public class PagedQueryParam implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
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
     * 复杂构造，适用于：1、自定义查询条件；2、多表联合查询
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
     * 前端查询表达式，包括分页、排序和筛选
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

    /**
     * 自定义数据查询规则，可用于数据权限处理
     */
    private FilterGroup filterGroup;

    public PagedQueryParam withDataRule(String key) {
        try {
            IDataRuleProvider provider = SpringContextHolder.getBean(IDataRuleProvider.class);
            this.filterGroup = provider.filter(key);
        } catch (NoSuchBeanDefinitionException exception) {
            logger.error("IDataRuleProvider未注入，自定义查询规则无效");
        }
        return this;
    }

    public PagedQueryParam withDataRule(IDataRuleProvider provider) {
        if (provider != null) {
            this.filterGroup = provider.filter(null);
        }
        return this;
    }

    String getDbFieldName(String field) {
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
        if (this.queryDescriptor == null)
            return this.where;
        else {
            List<FilterRule> queryRules = this.query == null || CollectionUtils.isEmpty(this.query.getFilters())
                    ? new ArrayList<>()
                    : this.query.getFilters();

            FilterGroup group = new FilterGroup(queryRules);
            group.and(this.filterGroup);

            String queryWhere = this.queryDescriptor.formatFilters(group);
            if (StringUtils.isBlank(queryWhere)) return this.where;
            else return StringUtils.isBlank(this.where) ? queryWhere : "(" +this.where + ") and " + queryWhere;
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
