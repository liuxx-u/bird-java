package com.bird.core.service.query;

import com.bird.core.service.AbstractDTO;

/**
 * Created by liuxx on 2017/6/22.
 */
public class FilterRule extends AbstractDTO {
    private String field;
    private String operate;
    private String value;

    public FilterRule(){}

    public FilterRule(String field,String operate,String value) {
        this.field = field;
        this.operate = operate;
        this.value = value;
    }

    public FilterRule(String key,String value) {
        this(key, FilterOperate.EQUAL, value);
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
