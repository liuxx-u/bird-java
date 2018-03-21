package com.bird.service.common.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;

/**
 * @author liuxx
 * @date 2018/3/19
 */
public class AbstractPureModel implements IModel {
    @TableId(type = IdType.AUTO)
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
