package com.bird.service.cms.impl;

import com.bird.core.Check;
//import com.bird.eventbus.EventBus;
//import com.bird.eventbus.handler.EventHandler;
import com.bird.core.utils.DozerHelper;
import com.bird.service.cms.CmsClassifyService;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.cms.model.CmsClassify;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.service.AbstractServiceImpl;
//import com.bird.service.zero.event.TestEventArg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "cms_classify")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.cms.CmsClassifyService")
public class CmsClassifyServiceImpl extends AbstractServiceImpl<CmsClassify> implements CmsClassifyService {

    @Autowired
    private DozerHelper dozerHelper;

//    @Autowired
//    private EventBus eventBus;

    /**
     * 获取分类
     *
     * @param id
     * @return
     */
    @Override
    public CmsClassifyDTO getClassify(Long id) {

        Check.GreaterThan(id, 0L, "id");
        CmsClassify classify = queryById(id);

//        eventBus.push(new TestEventArg());
        return dozerHelper.map(classify, CmsClassifyDTO.class);
    }

    /**
     * 保存分类
     *
     * @param dto
     */
    @Override
    public void saveClassify(CmsClassifyDTO dto) {
        Check.NotNull(dto, "dto");

        if (dto.getParentId() > 0) {
            CmsClassify parent = queryById(dto.getParentId());
            dto.setParentIds(parent.getParentIds() + "," + dto.getParentId());
        } else {
            dto.setParentIds("0");
        }
        CommonSaveParam param = new CommonSaveParam(dto, CmsClassifyDTO.class);
        save(param);
    }

//    @EventHandler
//    public void HandleEvent(TestEventArg eventArg) {
//        System.out.println("notify cms======");
//    }
}
