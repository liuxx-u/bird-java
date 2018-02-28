package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.eventbus.handler.EventHandler;
import com.bird.service.zero.FormService;
import com.bird.service.zero.dto.FormOperateDTO;
import com.bird.service.zero.event.TestEventArg;
import com.bird.service.zero.mapper.FormMapper;
import com.bird.service.zero.model.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "zero_form")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.FormService")
public class FormServiceImpl extends AbstractServiceImpl<Form> implements FormService {
    @Autowired
    private FormMapper formMapper;

    /**
     * 根据key获取表单信息
     *
     * @param key
     * @return
     */
    @Override
    public FormOperateDTO getFormByKey(String key) {
        Check.NotEmpty(key,"key");
        return formMapper.getFormByKey(key);
    }

    @EventHandler
    public void HandleEvent(TestEventArg eventArg) {
        System.out.println("notify zero======");
    }
}
