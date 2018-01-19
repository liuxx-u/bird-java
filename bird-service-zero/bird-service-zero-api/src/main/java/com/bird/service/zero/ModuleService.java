package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.dto.ModuleDTO;
import com.bird.service.zero.model.Module;

import java.util.List;

public interface ModuleService extends AbstractService<Module> {
    /**
     * 获取module信息
     * @param id moduleId
     * @return
     */
    ModuleDTO getModule(Long id);

    /**
     * 保存module信息
     * @param dto dto
     */
    void saveModule(ModuleDTO dto);
}
