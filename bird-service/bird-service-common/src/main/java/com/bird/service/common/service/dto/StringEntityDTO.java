package com.bird.service.common.service.dto;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class StringEntityDTO extends GenericEntityDTO<String> {
    public StringEntityDTO() {
        super();
        super.setId("");
    }
}
