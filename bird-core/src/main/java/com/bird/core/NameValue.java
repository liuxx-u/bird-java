package com.bird.core;

import java.io.Serializable;

/**
 *
 * @author liuxx
 * @date 2017/5/17
 */
public class NameValue implements Serializable {
    private String label;
    private String value;

    public NameValue (){}

    public NameValue(String label,String value) {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
