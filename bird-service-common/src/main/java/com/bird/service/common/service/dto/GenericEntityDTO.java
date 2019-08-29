package com.bird.service.common.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Data
public abstract class GenericEntityDTO<TKey extends Serializable> implements IEntityDTO<TKey> {

    private static final long serialVersionUID = 1L;

    private TKey id;
    private Date createTime;
}
