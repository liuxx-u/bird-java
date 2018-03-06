package com.bird.service.common.service.dto;

import com.bird.core.NameValue;

/**
 * Created by liuxx on 2017/10/20.
 */
public class OptionDTO extends NameValue {
    private boolean disable;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
