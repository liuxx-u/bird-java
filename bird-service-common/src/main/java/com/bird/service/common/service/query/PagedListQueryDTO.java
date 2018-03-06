package com.bird.service.common.service.query;

/**
 * Created by liuxx on 2017/6/23.
 */
public class PagedListQueryDTO extends ListQueryDTO {
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
