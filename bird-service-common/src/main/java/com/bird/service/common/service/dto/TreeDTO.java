package com.bird.service.common.service.dto;

/**
 * Created by liuxx on 2017/10/26.
 */
public class TreeDTO extends AbstractDTO {
    private String value;
    private String text;
    private String parentValue;

    public TreeDTO(String value,String text,String parentValue) {
        this.value = value;
        this.text = text;
        this.parentValue = parentValue;
    }

    public TreeDTO(String value,String text){
        this(value,text,"0");
    }

    public TreeDTO(){
        this("","");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentValue() {
        return parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }
}
