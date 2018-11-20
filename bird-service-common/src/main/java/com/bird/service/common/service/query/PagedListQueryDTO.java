package com.bird.service.common.service.query;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author liuxx
 * @date 2017/6/23
 */
@Getter
@Setter
public class PagedListQueryDTO extends ListQueryDTO {
    private int pageIndex;
    private int pageSize;
}
