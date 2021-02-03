package com.bird.service.common.service.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 筛选条件
 * @author liuxx
 * @date 2017/6/22
 */
@Getter
@Setter
public class FilterRule implements Serializable {
    private String field;
    private String operate;
    private String value;

    public FilterRule() {
    }

    public FilterRule(String field, String operate, String value) {
        this.field = field;
        this.operate = operate;
        this.value = value;
    }

    public FilterRule(String key, String value) {
        this(key, FilterOperate.EQUAL.getValue(), value);
    }
}
