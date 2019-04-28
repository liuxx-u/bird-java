package com.bird.service.zero.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bird.core.Check;
import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.MenuService;
import com.bird.service.zero.dto.MenuBriefDTO;
import com.bird.service.zero.dto.MenuDTO;
import com.bird.service.zero.mapper.MenuMapper;
import com.bird.service.zero.model.Menu;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxx on 2017/10/30.
 */
@Service
@CacheConfig(cacheNames = "zero_menu")
@org.apache.dubbo.config.annotation.Service
public class MenuServiceImpl extends AbstractService<MenuMapper,Menu> implements MenuService {

    /**
     * 根据id获取菜单信息
     *
     * @param menuId 菜单id
     * @return
     */
    @Override
    public MenuDTO getMenu(Long menuId) {
        Check.GreaterThan(menuId,0L,"menuId");
        Menu menu = queryById(menuId);
        return dozer.map(menu,MenuDTO.class);
    }

    /**
     * 获取所有菜单（测试用）
     * @return
     */
    public List<MenuBriefDTO> getAllMenus(){
        EntityWrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.where("delFlag = 0");
        wrapper.orderBy("orderNo");
        List<Menu> menus = selectList(wrapper);

        return dozer.mapList(menus,MenuBriefDTO.class);
    }
}
