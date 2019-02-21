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

        String sql = "select " + param.getSelect() + " from " + param.getFrom() + this.getTailSql(param);
        sql += " order by " + param.getDbFieldName(sortField) + (sortDirection == ListSortDirection.DESC ? " desc" : " asc")
                + "," + param.getDbFieldName("id") + " desc"
                + " limit " + (query.getPageIndex() - 1) * query.getPageSize() + "," + query.getPageSize();
        return sql;
    }

    public String queryTotalCount(PagedQueryParam param) {
        return  "select count(1) from " + param.getFrom() + this.getTailSql(param);
    }

    public String queryPagedSum(PagedQueryParam param) {
        PagedListQueryDTO query = param.getQuery();

        StringBuilder sb = new StringBuilder("select count(1) as totalCount");
        for (String field : query.getSumFields()) {
            sb.append(",").append("sum(").append(param.getDbFieldName(field)).append(") as ").append(field);
        }
        sb.append(" from ").append(param.getFrom()).append(this.getTailSql(param));
        return sb.toString();
    }

    private String getTailSql(PagedQueryParam param){
        return where(param) + param.getAppendSql();
    }

    private String where(PagedQueryParam param) {
        String where = param.getWhere();
        if (StringUtils.isBlank(where)) {
            where = param.isFilterSoftDelete() ? " delFlag = 0 " : "";
        } else {
            where = param.isFilterSoftDelete() ? " delFlag = 0 and (" + where + ")" : where;
        }

        if(StringUtils.isNotBlank(where)){
            where = " where " + where;
        }
        return where;
    }
}
