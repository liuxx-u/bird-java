package com.bird.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.zero.MenuService;
import com.bird.service.zero.dto.MenuBriefDTO;
import com.bird.service.zero.dto.MenuDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liuxx on 2017/10/30.
 */
@Api(description = "系统-菜单接口")
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends AbstractController {

    @Reference
    private MenuService menuService;

    @GetMapping(value = "/getAllMenus")
    public OperationResult<List<MenuBriefDTO>> getAllMenus() {

        List<MenuBriefDTO> result = menuService.getAllMenus();
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/getTreeData")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`","`name`","`parentId`");
        param.setFrom("`zero_menu`");
        List<TreeDTO> result = menuService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/getMenu")
    public OperationResult<MenuDTO> getMenu(Long id) {
        Check.GreaterThan(id, 0L, "id");
        MenuDTO result = menuService.getMenu(id);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody MenuDTO dto){
        CommonSaveParam param = new CommonSaveParam(dto, MenuDTO.class);
        menuService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/delete")
    public OperationResult delete(Long id){
        Check.GreaterThan(id,0L,"id");
        menuService.delete(id);
        return OperationResult.Success("删除成功",null);
    }
}
