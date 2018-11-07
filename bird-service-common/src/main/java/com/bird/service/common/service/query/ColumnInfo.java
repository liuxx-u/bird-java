package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;

/**
 * @author liuxx
 * @date 2018/11/6
 */
public class ColumnInfo extends AbstractDTO {
    private String field;
    private String name;
    private String type;
    private String sourceUrl;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
