package com.bird.service.common.model;

import java.io.Serializable;

/**
 * @author liuxx
 * @date 2018/2/27
 */
public interface IModel extends Serializable {

    /**
     * 获取id
     * @return
     */
    Long getId();

    /**
     * 设置id
     * @param id
     */
    void setId(Long id);
}
