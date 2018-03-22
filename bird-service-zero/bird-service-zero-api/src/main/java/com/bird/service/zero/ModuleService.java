package com.bird.service.zero;

import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.ModuleDTO;
import com.bird.service.zero.model.Module;


public interface ModuleService extends IService<Module> {
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
