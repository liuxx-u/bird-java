package com.bird.core.service;

/**
 * Created by liuxx on 2017/10/16.
 */
@SuppressWarnings("serial")
public abstract class EntityDTO extends AbstractDTO {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
