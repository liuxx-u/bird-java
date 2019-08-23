package com.bird.service.common.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Getter
@Setter
public abstract class GenericEntityDTO<TKey extends Serializable> extends AbstractDTO {

    private TKey id;
    private Date createTime;
}
