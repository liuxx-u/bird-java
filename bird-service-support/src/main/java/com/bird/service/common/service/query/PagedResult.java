package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractBO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/3/30
 */
@Getter
@Setter
public class PagedResult<T> extends AbstractBO {
    private Long totalCount;
    private List<T> items;
    private Map<String, Object> sum;

    public PagedResult() {
        totalCount = 0L;
        items = new ArrayList<>();
        sum = new HashMap<>();
    }

    public PagedResult(Long totalCount, List<T> items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public PagedResult(Long totalCount, List<T> items, Map<String, Object> sum) {
        this.totalCount = totalCount;
        this.items = items;
        this.sum = sum;
    }
}
