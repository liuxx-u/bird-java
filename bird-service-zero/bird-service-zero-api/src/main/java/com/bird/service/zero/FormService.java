package com.bird.service.zero;

import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.dto.FormOperateDTO;
import com.bird.service.zero.model.Form;

public interface FormService extends AbstractService<Form> {

    /**
     * 根据key获取表单信息
     * @param key
     * @return
     */
    FormOperateDTO getFormByKey(String key);
}
