package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.model.DicType;

import java.util.List;

/**
 * Created by liuxx on 2017/11/3.
 */
public interface DicTypeService extends AbstractService<DicType> {

    /**
     * 获取字典类型 树形数据
     * @return
     */
    List<TreeDTO> getDicTypeTreeData();
}
