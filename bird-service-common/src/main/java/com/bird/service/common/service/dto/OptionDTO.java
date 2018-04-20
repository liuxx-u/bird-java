package com.bird.service.common.service.dto;

import com.bird.core.NameValue;

/**
 *
 * @author liuxx
 * @date 2017/10/20
 */
public class OptionDTO extends NameValue {
    private Boolean disable;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
