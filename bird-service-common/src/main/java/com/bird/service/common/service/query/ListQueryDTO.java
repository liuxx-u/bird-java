package com.bird.service.common.service.query;

import com.bird.service.common.mapper.permission.DataAuthority;
import com.bird.service.common.service.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/6/23.
 */
public class ListQueryDTO extends AbstractDTO {
    private String sortField;
    private int sortDirection;
    private List<FilterRule> filters;

    private DataAuthority authority;//数据权限参数

    public ListQueryDTO() {
        filters = new ArrayList<>();
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

    public List<FilterRule> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterRule> filters) {
        this.filters = filters;
    }

    public DataAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(DataAuthority authority) {
        this.authority = authority;
    }
}
