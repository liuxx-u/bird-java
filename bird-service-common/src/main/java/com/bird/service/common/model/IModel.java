package com.bird.service.common.model;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/2/27
 */
public interface IModel<PK> extends Serializable {
    PK getId();

    void setId(PK id);
}
