package com.bird.service.common.model;

/**
 * @author liuxx
 * @date 2018/2/27
 *
 *
 */
public abstract class AbstractModel<PK> implements IModel<PK> {
    private PK id;

    @Override
    public PK getId() {
        return id;
    }

    @Override
    public void setId(PK id) {
        this.id = id;
    }
}
