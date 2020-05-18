package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/11/6
 */
@Getter
@Setter
public class ExportQuery extends AbstractDTO {
    /**
     * 标题
     */
    private String title;
    /**
     * 查询地址
     */
    private String url;
    /**
     * 查询条件
     */
    private PagedListQuery query;
    /**
     * 列信息
     */
    private List<ColumnInfo> columns;

    public ExportQuery(){
        columns = new ArrayList<>();
    }
}
