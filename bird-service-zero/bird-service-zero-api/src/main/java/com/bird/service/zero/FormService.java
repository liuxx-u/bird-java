package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.core.service.TreeDTO;
import com.bird.core.utils.StringHelper;
import com.bird.service.zero.dto.FormOperateDTO;
import com.bird.service.zero.model.Form;

import java.util.List;

public interface FormService extends AbstractService<Form> {

    /**
     * 根据key获取表单信息
     * @param key
     * @return
     */
    FormOperateDTO getFormByKey(String key);
}
