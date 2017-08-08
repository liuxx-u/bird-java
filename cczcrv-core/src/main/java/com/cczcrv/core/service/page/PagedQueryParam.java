package com.cczcrv.core.service.page;

import com.cczcrv.core.service.SerializableDTO;

/**
 * Created by liuxx on 2017/7/6.
 */
public class PagedQueryParam extends SerializableDTO {
    private String sortField;
    private int sortDirection;//0:正序;1:倒序
    private int pageIndex;
    private int pageSize;

    public PagedQueryParam(){}


    public PagedQueryParam(int pageIndex,int pageSize) {
        this(pageIndex, pageSize, "id");
    }

    public PagedQueryParam(int pageIndex,int pageSize,String sortField) {
        this(pageIndex, pageSize, sortField, 1);
    }

    public PagedQueryParam(int pageIndex,int pageSize,String sortField,int sortDirection) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }



    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public int getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

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
