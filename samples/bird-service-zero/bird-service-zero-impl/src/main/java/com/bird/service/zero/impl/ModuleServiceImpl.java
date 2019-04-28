package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.ModuleService;
import com.bird.service.zero.dto.ModuleDTO;
import com.bird.service.zero.mapper.ModuleMapper;
import com.bird.service.zero.model.Module;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "zero_module")
@org.apache.dubbo.config.annotation.Service
public class ModuleServiceImpl extends AbstractService<ModuleMapper,Module> implements ModuleService {

    /**
     * 获取module信息
     *
     * @param id moduleId
     * @return
     */
    @Override
    public ModuleDTO getModule(Long id) {
        Check.GreaterThan(id, 0L, "id");
        Module module = queryById(id);
        return dozer.map(module, ModuleDTO.class);
    }

    /**
     * 保存module信息
     *
     * @param dto dto
     */
    @Override
    public void saveModule(ModuleDTO dto) {
        Check.NotNull(dto,"dto");
        CommonSaveParam param = new CommonSaveParam(dto, ModuleDTO.class);
        save(param);
    }
}
