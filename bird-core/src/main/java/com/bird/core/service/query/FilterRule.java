package com.bird.core.service.query;

import com.bird.core.service.AbstractDTO;

/**
 * Created by liuxx on 2017/6/22.
 */
public class FilterRule extends AbstractDTO {
    private String key;
    private String operate;
    private String value;

    public FilterRule(){}

    public FilterRule(String key,String operate,String value) {
        this.key = key;
        this.operate = operate;
        this.value = value;
    }

    public FilterRule(String key,String value) {
        this(key, FilterOperate.EQUAL, value);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operator) {
        this.operate = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
