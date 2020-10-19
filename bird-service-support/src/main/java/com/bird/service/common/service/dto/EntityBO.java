package com.bird.service.common.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public abstract class EntityBO<TKey extends Serializable> extends AbstractBO implements IEntityBO<TKey> {

    private static final long serialVersionUID = 1L;

    private TKey id;
    private Date createTime;
}
