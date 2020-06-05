package com.bird.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuxx
 * @since 2020/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeNode extends NameValue {
    /**
     * 父节点Value
     */
    private String parentValue;

    /**
     * true则强制渲染为文件夹，前端效果为：子元素为空则不渲染该节点
     */
    private Boolean folder;

    public TreeNode() {
    }

    public TreeNode(String label, String value) {
        super(label, value);
    }

    public TreeNode(String label, String value, String parentValue) {
        super(label, value);
        this.parentValue = parentValue;
    }
}
