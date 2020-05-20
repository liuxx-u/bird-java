package com.bird.service.common.service.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class StringEntityBO extends EntityBO<String> {
    public StringEntityBO() {
        super();
        super.setId(StringUtils.EMPTY);
    }
}
