package com.bird.service.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bird.service.common.model.IPO;

/**
 *
 * @author liuxx
 * @date 2017/5/15
 */
public interface AbstractMapper<T extends IPO> extends BaseMapper<T> {
}
