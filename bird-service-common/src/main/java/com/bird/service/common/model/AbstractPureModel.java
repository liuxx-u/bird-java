package com.bird.service.common.model;

/**
 * @author liuxx
 * @date 2018/3/19
 */
public class AbstractPureModel implements IModel {
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
