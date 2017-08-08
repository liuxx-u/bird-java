package com.cczcrv.service.cms.mapper.provider;

import com.cczcrv.core.service.query.ListSortDirection;
import com.cczcrv.core.utils.StringHelper;
import com.cczcrv.service.cms.dto.CmsQueryDTO;

import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
public class QueryContentProvider {
    public String QueryContentBriefList(CmsQueryDTO query) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT Id AS id,Title AS title,Cover AS cover,[Link] AS link,BrowseNo AS browseNo,PraiseNo AS praiseNo FROM Cms_Content");
        sql.append(where(query));

        String sortField = "OrderNo";
        int sortDirection = ListSortDirection.ASC;//默认正序
        if (!StringHelper.isNullOrWhiteSpace(query.getSortField())) {
            sortField = query.getSortField();
            sortDirection = query.getSortDirection();
        }
        sql.append(" ORDER BY " + sortField + "");
        if (sortDirection == ListSortDirection.DESC) {
            sql.append(" DESC");
        }

        int pageIndex = query.getPageIndex();
        int pageSize = query.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0 || pageSize > 50) pageSize = 15;//一次查询最多获取50条数据,15为默认每页数量。

        sql.append(" OFFSET " + (pageIndex - 1) * pageSize + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY");
        return sql.toString();
    }

    public String SqlContent(List<Long> ids) {
        String idsStr = "";
        for (Long i : ids) {
            if (idsStr == "") {
                idsStr += i;
            } else {
                idsStr += "," + i;
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select ContentId,Path from Content_Asset where ContentId in(" + idsStr + ")  order by newid()");
        return sql.toString();
    }


    private String where(CmsQueryDTO query) {
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE IsPublish=1 AND IsDeleted=0");

        int classifyId = query.getClassifyId();
        if (classifyId > 0) {
            sql.append(" AND ClassifyId = #{classifyId}");
        }
        String queryKey = query.getQueryKey();
        if (!StringHelper.isNullOrWhiteSpace(queryKey)) {
            sql.append(" AND Title LIKE '%'+#{queryKey}+'%'");
        }
        return sql.toString();
    }
}
