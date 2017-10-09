package com.bird.core.service.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/6/23.
 */
public class QueryListRequestInput {
    private String sortField;
    private int sortDirection;
    private List<FilterRule> filters;

    public QueryListRequestInput() {
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
}
