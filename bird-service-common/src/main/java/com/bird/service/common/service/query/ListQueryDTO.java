package com.bird.service.common.service.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.bird.service.common.mapper.permission.DataAuthority;
import com.bird.service.common.service.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author liuxx
 * @date 2017/6/23
 */
@Getter
@Setter
public class ListQueryDTO extends AbstractDTO {
    private String sortField;
    private int sortDirection;
    private List<FilterRule> filters;
    private List<String> sumFields;
    /**
     * 数据权限参数
     */
    @JSONField(serialize=false)
    private DataAuthority authority;

    public ListQueryDTO() {
        filters = new ArrayList<>();
        sumFields = new ArrayList<>();
    }
}
