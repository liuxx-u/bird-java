package com.bird.service.common.grid.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author liuxx
 * @date 2017/6/23
 */
@Getter
@Setter
public class PagedListQuery extends ListQuery {
    private int pageIndex;
    private int pageSize;
}
