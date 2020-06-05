package com.bird.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 选项
 *
 * @author liuxx
 * @since 2020/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Option extends NameValue {

    /**
     * 是否禁用
     */
    private Boolean disabled;

    public Option() {
    }

    public Option(String label, String value) {
        super(label, value);
    }
}
