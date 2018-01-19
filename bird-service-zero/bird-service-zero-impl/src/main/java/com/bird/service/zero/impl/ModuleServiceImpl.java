package com.bird.service.zero.impl;

import com.bird.core.Check;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.service.AbstractServiceImpl;
import com.bird.core.service.TreeDTO;
import com.bird.core.utils.DozerHelper;
import com.bird.service.zero.ModuleService;
import com.bird.service.zero.dto.ModuleDTO;
import com.bird.service.zero.dto.OrganizationDTO;
import com.bird.service.zero.mapper.ModuleMapper;
import com.bird.service.zero.model.Module;
import com.bird.service.zero.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "zero_module")
@com.alibaba.dubbo.config.annotation.Service(interfaceName = "com.bird.service.zero.ModuleService")
public class ModuleServiceImpl extends AbstractServiceImpl<Module> implements ModuleService {
    @Autowired
    private DozerHelper dozerHelper;

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
        return dozerHelper.map(module, ModuleDTO.class);
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
