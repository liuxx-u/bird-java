package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2018/11/6
 */
public class ExportQueryDTO extends AbstractDTO {
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
    private PagedListQueryDTO query;
    /**
     * 列信息
     */
    private List<ColumnInfo> columns;

    public ExportQueryDTO(){
        columns = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PagedListQueryDTO getQuery() {
        return query;
    }

    public void setQuery(PagedListQueryDTO query) {
        this.query = query;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }
}
