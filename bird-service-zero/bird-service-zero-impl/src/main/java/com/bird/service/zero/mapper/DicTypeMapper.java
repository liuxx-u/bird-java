package com.bird.service.zero.mapper;

import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.zero.dto.DicDTO;
import com.bird.service.zero.model.DicType;
import org.apache.ibatis.annotations.Param;


/**
 * Created by liuxx on 2017/11/3.
 */
public interface DicTypeMapper extends AbstractMapper<DicType> {

    /**
     * 根据key获取字典信息
     * @return
     */
    DicDTO getDicByKey(@Param("key")String key);
}
