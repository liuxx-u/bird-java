package com.bird.service.zero.mapper;

import com.bird.core.mapper.AbstractMapper;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.dto.FormOperateDTO;
import com.bird.service.zero.model.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormMapper extends AbstractMapper<Form> {
    /**
     * 根据key获取表单信息
     *
     * @param key
     * @return
     */
    FormOperateDTO getFormByKey(@Param("key") String key);
}
