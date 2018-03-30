package com.bird.service.common.service.query;

import java.util.List;
import java.util.Map;

/**
 * Created by liuxx on 2017/10/11.
 */
public class PagedListResultDTO extends PagedResult<Map> {

    public PagedListResultDTO() {
        super();
    }

    public PagedListResultDTO(Long totalCount, List<Map> items) {
        super(totalCount, items);
    }
}
