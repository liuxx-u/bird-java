package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@KeySequence(value = "SEQ_UUID_STRING_KEY", clazz = String.class)
public abstract class StringPureModel implements IModel<String> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 获取id
     *
     * @return id
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
}
