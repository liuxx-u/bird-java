package com.bird.service.common.mapper;

import com.bird.service.common.service.query.ListSortDirection;
import com.bird.service.common.service.query.PagedListQueryDTO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author liuxx
 * @date 2017/10/10
 */
public class PagedQueryProvider {

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
        sql += " order by " + param.getDbFieldName(sortField) + (sortDirection == ListSortDirection.DESC ? " desc" : " asc")
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
        String where = param.getWhere();
        if (StringUtils.isBlank(where)) {
            return param.isFilterSoftDelete() ? " delFlag = 0 " : "";
        } else {
            return param.isFilterSoftDelete() ? " delFlag = 0 and " + where : where;
        }
    }
}
