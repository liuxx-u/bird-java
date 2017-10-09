package com.bird.common.dto;

import com.bird.core.service.SerializableDTO;

/**
 * Created by liuxx on 2017/6/27.
 */
public class AttributeValueDTO extends SerializableDTO {
    private Long id;
    private String key;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
