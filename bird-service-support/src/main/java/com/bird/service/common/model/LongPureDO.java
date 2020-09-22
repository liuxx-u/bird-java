package com.bird.service.common.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Data
public abstract class LongPureDO implements IDO<Long> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
}
