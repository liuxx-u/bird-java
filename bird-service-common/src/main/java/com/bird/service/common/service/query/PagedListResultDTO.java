package com.bird.service.common.service.query;


import com.bird.service.common.service.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxx on 2017/10/11.
 */
public class PagedListResultDTO extends AbstractDTO {
    private Long totalCount;
    private List<Map> items;

    public PagedListResultDTO(){
        items = new ArrayList<>();
    }

    public PagedListResultDTO(Long totalCount,List<Map> items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<Map> items) {
        this.items = items;
    }
}
