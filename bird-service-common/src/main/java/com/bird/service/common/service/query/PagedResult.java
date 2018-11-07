package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/3/30
 */
public class PagedResult<T> extends AbstractDTO {
    private Long totalCount;
    private List<T> items;
    private Map<String,Number> sum;

    public PagedResult() {
        totalCount = 0L;
        items = new ArrayList<>();
        sum = new HashMap<>();
    }

    public PagedResult(Long totalCount, List<T> items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public PagedResult(Long totalCount, List<T> items,Map<String,Number> sum) {
        this.totalCount = totalCount;
        this.items = items;
        this.sum = sum;
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

    public Map<String, Number> getSum() {
        return sum;
    }

    public void setSum(Map<String, Number> sum) {
        this.sum = sum;
    }
}
