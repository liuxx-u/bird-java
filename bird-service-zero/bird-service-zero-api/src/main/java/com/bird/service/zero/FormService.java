package com.bird.service.zero;

import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.FormOperateDTO;
import com.bird.service.zero.model.Form;

public interface FormService extends IService<Form> {

    /**
     * 根据key获取表单信息
     * @param key
     * @return
     */
    FormOperateDTO getFormByKey(String key);
}
