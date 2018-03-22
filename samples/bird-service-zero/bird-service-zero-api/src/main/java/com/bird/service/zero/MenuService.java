package com.bird.service.zero;

import com.bird.service.common.service.IService;
import com.bird.service.zero.dto.MenuBriefDTO;
import com.bird.service.zero.dto.MenuDTO;
import com.bird.service.zero.model.Menu;

import java.util.List;

/**
 * Created by liuxx on 2017/10/30.
 */
public interface MenuService  extends IService<Menu> {

    /**
     * 根据id获取菜单信息
     * @return
     */
    MenuDTO getMenu(Long menuId);

    /**
     * 获取所有菜单（测试用）
     * @return
     */
    List<MenuBriefDTO> getAllMenus();
}
