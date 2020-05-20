package com.bird.service.common.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author liuxx
 * @date 2019/8/22
 */
public abstract class StringPureDO implements IDO<String> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
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
