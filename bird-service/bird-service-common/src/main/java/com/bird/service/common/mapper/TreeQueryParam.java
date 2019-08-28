package com.bird.service.common.mapper;

import java.io.Serializable;

/**
 * @author liuxx
 */
public class TreeQueryParam implements Serializable {
    private String valueField;
    private String textField;
    private String parentValueField;
    private String from;
    private String where;
    private String orderBy;

    public TreeQueryParam(){
        this("`id`","`name`");
    }

    public TreeQueryParam(String valueField,String textField){
        this(valueField,textField,"");
    }

    public TreeQueryParam(String valueField,String textField,String parentValueField) {
        this.valueField = valueField;
        this.textField = textField;
        this.parentValueField = parentValueField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public String getParentValueField() {
        return parentValueField;
    }

    public void setParentValueField(String parentValueField) {
        this.parentValueField = parentValueField;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}