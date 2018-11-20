package com.bird.service.common.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author liuxx
 * @date 2017/10/26
 */
@Getter
@Setter
public class TreeDTO extends AbstractDTO {
    private String value;
    private String label;
    private String parentValue;

    /**
     * true则强制渲染为文件夹，前端效果为：子元素为空则不渲染该节点
     */
    private Boolean folder;

    public TreeDTO(String value,String label,String parentValue) {
        this.value = value;
        this.label = label;
        this.parentValue = parentValue;
    }

    public TreeDTO(String value,String label){
        this(value,label,"0");
    }

    public TreeDTO(){
        this("","");
    }
}
