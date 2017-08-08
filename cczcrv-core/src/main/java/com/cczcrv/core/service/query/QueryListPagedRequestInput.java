package com.cczcrv.core.service.query;

/**
 * Created by liuxx on 2017/6/23.
 */
public class QueryListPagedRequestInput extends QueryListRequestInput {
    private int pageIndex;
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
