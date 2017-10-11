package com.bird.core;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by liuxx on 2017/5/17.
 */
public class NameValue {
    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "Value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
