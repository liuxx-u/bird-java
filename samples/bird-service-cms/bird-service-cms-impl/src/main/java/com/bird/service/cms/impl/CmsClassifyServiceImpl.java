package com.bird.service.cms.impl;

import com.bird.core.Check;
import com.bird.eventbus.EventBus;
import com.bird.eventbus.handler.EventHandler;
import com.bird.service.cms.CmsClassifyService;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.cms.mapper.CmsClassifyMapper;
import com.bird.service.cms.model.CmsClassify;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.service.AbstractService;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import com.bird.service.zero.event.TestEventArg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "cms_classify")
@org.apache.dubbo.config.annotation.Service
public class CmsClassifyServiceImpl extends AbstractService<CmsClassifyMapper,CmsClassify> implements CmsClassifyService {

    @Autowired
    private EventBus eventBus;

    @Override
    public PagedListResultDTO queryPagedList(PagedListQueryDTO queryDTO, Class cls) {
        PagedQueryParam param = new PagedQueryParam(queryDTO,cls).withDataRule();
        return super.queryPagedList(param);
    }

    /**
     * 获取分类
     *
     * @param id
     * @return
     */
    @Override
    public CmsClassifyDTO getClassify(Long id) {

//        Check.GreaterThan(id, 0L, "id");
//        CmsClassify classify = queryById(id);

//        eventBus.push(new TestEventArg());

//        CmsClassify classify = new CmsClassify();
//        classify.setName("test");
//        classify.setParentId(0L);
//
//        save(classify);
//        throw new UserFriendlyException("error");

        return new CmsClassifyDTO();
//        return dozer.map(classify, CmsClassifyDTO.class);
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

    @EventHandler
    @Transactional(rollbackFor = Exception.class)
    public void HandleEvent(TestEventArg eventArg) {
//        CmsClassify classify = new CmsClassify();
//        classify.setName("test");
//        classify.setParentId(0L);
//
//        save(classify);

        System.out.println("notify cms======");
    }
}
