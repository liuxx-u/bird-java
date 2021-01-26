package com.bird.service.common.mapper;

import com.bird.service.common.grid.query.ListSortDirection;
import com.bird.service.common.grid.query.PagedListQuery;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author liuxx
 * @date 2017/10/10
 */
@Deprecated
public class PagedQueryProvider {

    public String queryPagedList(PagedQueryParam param) {
        PagedListQuery query = param.getQuery();

        //默认Id倒序
        String sortField = "id";
        int sortDirection = ListSortDirection.DESC;
        if (StringUtils.isNotBlank(query.getSortField())) {
            sortField = query.getSortField();
            sortDirection = query.getSortDirection();
        }

        String sql = "select " + param.getSelect() + " from " + param.getFrom() + this.getTailSql(param);
        sql += " order by " + param.getDbFieldName(sortField) + (sortDirection == ListSortDirection.DESC ? " desc" : " asc")
                + "," + param.getDbFieldName("id") + " desc"
                + " limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize();
        return sql;
    }

    public String queryPagedSum(PagedQueryParam param) {
        StringBuilder sumSql = this.getSumSql(param, true);
        sumSql.append(" from ").append(param.getFrom()).append(this.getTailSql(param));

        if (StringUtils.containsIgnoreCase(param.getAppendSql(), "group by")) {
            StringBuilder groupSumSql = this.getSumSql(param, false);
            groupSumSql.append(" from (").append(sumSql.toString()).append(") as temp");
            return groupSumSql.toString();
        }

        return sumSql.toString();
    }

    private StringBuilder getSumSql(PagedQueryParam param, Boolean useAlias) {
        StringBuilder sb = new StringBuilder("select count(1) as totalCount");
        for (String field : param.getQuery().getSumFields()) {
            sb.append(",")
                    .append("sum(")
                    .append(useAlias ? param.getDbFieldName(field) : field)
                    .append(") as ")
                    .append(field);
        }
        return sb;
    }

    private String getTailSql(PagedQueryParam param) {
        return where(param) + param.getAppendSql();
    }

    private String where(PagedQueryParam param) {
        String where = param.getWhere();
        if (StringUtils.isBlank(where)) {
            where = param.isFilterSoftDelete() ? " delFlag = 0 " : "";
        } else {
            where = param.isFilterSoftDelete() ?  " (" + where + ") and delFlag = 0": where;
        }

        if (StringUtils.isNotBlank(where)) {
            where = " where " + where;
        }
        return where;
    }
}
