package com.bird.service.common.service.query;

import java.io.Serializable;

/**
 *
 * @author liuxx
 * @date 2017/6/22
 */
public class FilterRule implements Serializable {
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
