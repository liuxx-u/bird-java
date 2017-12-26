package com.bird.core.service;

import java.util.Date;

/**
 * Created by liuxx on 2017/10/16.
 */
@SuppressWarnings("serial")
public abstract class EntityDTO extends AbstractDTO {
    private Long id;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
