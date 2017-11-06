package com.bird.service.zero;

import com.bird.core.service.AbstractService;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.dto.MenuBriefDTO;
import com.bird.service.zero.dto.MenuDTO;
import com.bird.service.zero.model.Menu;

import java.util.List;

/**
 * Created by liuxx on 2017/10/30.
 */
public interface MenuService  extends AbstractService<Menu> {

    /**
     * 获取菜单 树形数据
     * @return
     */
    List<TreeDTO> getMenuTreeData();

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
