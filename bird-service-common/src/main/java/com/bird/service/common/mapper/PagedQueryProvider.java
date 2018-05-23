package com.bird.service.common.mapper;

import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import com.bird.service.common.service.query.ListSortDirection;
import com.bird.service.common.service.query.PagedListQueryDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author liuxx
 * @date 2017/10/10
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

        //默认Id倒序
        String sortField = "id";
        int sortDirection = ListSortDirection.DESC;
        if (StringUtils.isNotBlank(query.getSortField())) {
            sortField = query.getSortField();
            sortDirection = query.getSortDirection();
        }

        String sql = "select " + param.getSelect() + " from " + param.getFrom();
        String whereSql = where(param);
        if (StringUtils.isNotBlank(whereSql)) {
            sql += " where " + whereSql;
        }
        sql += " order by " + this.getDbFieldName(param, sortField) + (sortDirection == ListSortDirection.DESC ? " desc" : " asc")
                + " limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize();
        return sql;
    }

    public String queryTotalCount(PagedQueryParam param) {
        String sql = "select count(1) from " + param.getFrom();
        String whereSql = where(param);
        if (StringUtils.isNotBlank(whereSql)) {
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
            if (!validateSqlValue(value)) continue;

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
        fieldName = String.format("'%s'",fieldName);
        Map<String, String> fieldMap = param.getFieldMap();
        return fieldMap.getOrDefault(fieldName, fieldName);
    }

    /**
     * 验证用户输入的值是否合法
     * @param value
     * @return
     */
    private boolean validateSqlValue(String value) {
        String[] illegals = new String[]{"'", "and", "exec", "insert", "select", "delete", "update", "count", "*", "%", "chr", "mid", "master", "truncate", "char", "declare", ";", "or", "-", "+", ","};
        for (String str : illegals) {
            if (value.contains(str)) return false;
        }
        return true;
    }
}
