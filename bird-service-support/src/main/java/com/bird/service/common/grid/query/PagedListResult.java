package com.bird.service.common.grid.query;

import java.util.List;
import java.util.Map;

/**
 *
 * @author liuxx
 * @date 2017/10/11
 */
public class PagedListResult extends PagedResult<Map> {

    public PagedListResult() {
        super();
    }

    public PagedListResult(Long totalCount, List<Map> items) {
        super(totalCount, items);
    }

    public PagedListResult(Long totalCount, List<Map> items, Map<String, Object> sum) {
        super(totalCount, items, sum);
    }
}
