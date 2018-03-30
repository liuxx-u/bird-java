package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/3/30
 */
public class PagedResult<T> extends AbstractDTO {
    private Long totalCount;
    private List<T> items;

    public PagedResult() {
        totalCount = 0L;
        items = new ArrayList<>();
    }

    public PagedResult(Long totalCount, List<T> items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
