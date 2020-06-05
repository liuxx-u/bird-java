package com.bird.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/6/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeNode2 extends NameValue {
    /**
     * 叶子节点
     */
    private List<NameValue> children;


    public TreeNode2() {
        this.children = new ArrayList<>();
    }

    public TreeNode2(String label, String value) {
        super(label, value);
        this.children = new ArrayList<>();
    }
}
