package com.bird.core.mapper;

import com.bird.core.model.AbstractModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by liuxx on 2017/5/15.
 */
public interface AbstractMapper<T extends AbstractModel> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

    List<Long> selectIdPage(RowBounds rowBounds, @Param("cm") Map<String, Object> params);
}
