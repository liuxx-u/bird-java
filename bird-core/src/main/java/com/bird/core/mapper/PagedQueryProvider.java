package com.bird.core.mapper;

import com.bird.core.service.query.FilterOperate;
import com.bird.core.service.query.FilterRule;
import com.bird.core.service.query.ListSortDirection;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.utils.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxx on 2017/10/10.
 */
public class PagedQueryProvider {
    private static final Map<String, String> OperateMap;

    static {
        OperateMap = new HashMap<>();
        OperateMap.put(FilterOperate.EQUAL, "=");
        OperateMap.put(FilterOperate.NOTEQUAL, "!=");
        OperateMap.put(FilterOperate.LESS, "<");
        OperateMap.put(FilterOperate.LESSOREQUAL, "<=");
        OperateMap.put(FilterOperate.GREATER, ">");
        OperateMap.put(FilterOperate.GREATEROREQUAL, ">=");
        OperateMap.put(FilterOperate.STARTWITH, "like");
        OperateMap.put(FilterOperate.ENDWITH, "like");
        OperateMap.put(FilterOperate.CONTAINS, "like");
    }

    public String queryPagedList(PagedQueryParam param) {
        PagedListQueryDTO query = param.getQuery();

        String sortField = "id";
        int sortDirection = ListSortDirection.DESC;//默认Id倒序
        if (!StringUtils.isBlank(query.getSortField())) {
            sortField = query.getSortField();
            sortDirection = query.getSortDirection();
        }

        String sql = "select " + param.getSelect() + " from " + param.getFrom();
        String whereSql = where(param);
        if (!StringUtils.isBlank(whereSql)) {
            sql += " where " + whereSql;
        }
        sql += " order by " + this.getDbFieldName(param, sortField) + (sortDirection == ListSortDirection.DESC ? " desc" : " asc")
                + " limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize();
        return sql;
    }

    public String queryTotalCount(PagedQueryParam param) {
        String sql = "select count(1) from " + param.getFrom();
        String whereSql = where(param);
        if (!StringUtils.isBlank(whereSql)) {
            sql += " where " + whereSql;
        }
        return sql;
    }

    private String where(PagedQueryParam param) {
        List<FilterRule> rules = param.getQuery().getFilters();
        boolean filterSoftDelete = param.isFilterSoftDelete();
        String customWhere = param.getWhere();

        boolean isStart = true;
        StringBuilder sb = new StringBuilder();
        if (filterSoftDelete) {
            sb.append(" delFlag = 0");
            isStart = false;
        }
        if (StringUtils.isNotBlank(customWhere)) {
            if (!isStart) {
                sb.append(" and ");
            }
            sb.append(customWhere);
            isStart = false;
        }

        for (FilterRule rule : rules) {
            String value = rule.getValue();
            if (StringUtils.isBlank(value)) continue;

            if (!OperateMap.containsKey(rule.getOperate())) {
                rule.setOperate(FilterOperate.EQUAL);
            }
            if (!isStart) {
                sb.append(" and ");
            }
            //处理value
            if (rule.getOperate().equals(FilterOperate.STARTWITH)) {
                value = value + "%";
            } else if (rule.getOperate().equals(FilterOperate.ENDWITH)) {
                value = "%" + value;
            } else if (rule.getOperate().equals(FilterOperate.CONTAINS)) {
                value = "%" + value + "%";
            }

            sb.append(this.getDbFieldName(param, rule.getField()) + OperateMap.get(rule.getOperate()) + " '" + value + "'");
            isStart = false;
        }
        return sb.toString();
    }

    private String getDbFieldName(PagedQueryParam param, String fieldName) {
        Map<String, String> fieldMap = param.getFieldMap();
        String dbFieldName = fieldMap.getOrDefault(fieldName, fieldName);

        if (!StringUtils.startsWith(dbFieldName, "`")) {
            dbFieldName = "`" + dbFieldName;
        }
        if (!StringUtils.endsWith(dbFieldName, "`")) {
            dbFieldName = dbFieldName + "`";
        }
        return dbFieldName;
    }
}
