package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.service.zero.dto.DicDTO;
import com.bird.service.zero.dto.DicTypeDTO;
import com.bird.service.zero.model.DicType;

/**
 * Created by liuxx on 2017/11/3.
 */
public interface DicTypeService extends AbstractService<DicType> {

    /**
     * 根据key获取字典信息
     * @return
     */
    DicDTO getDicByKey(String key);

    /**
     * 获取字典信息
     * @return
     */
    DicTypeDTO getDicType(Long id);
}
