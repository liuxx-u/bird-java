package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.MenuService;
import com.bird.service.zero.dto.MenuDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuxx on 2017/10/30.
 */
@Api(description = "系统-菜单接口")
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends AbstractController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/getTreeData", method = {RequestMethod.GET})
    public OperationResult<List<TreeDTO>> getTreeData() {
        List<TreeDTO> result = menuService.getMenuTreeData();
        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/getMenu", method = {RequestMethod.GET})
    public OperationResult<MenuDTO> getMenu(Long id) {
        Check.GreaterThan(id, 0L, "id");
        MenuDTO result = menuService.getMenu(id);
        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public OperationResult save(@RequestBody MenuDTO dto){
        CommonSaveParam param = new CommonSaveParam(dto, MenuDTO.class);
        menuService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    public OperationResult delete(Long id){
        Check.GreaterThan(id,0L,"id");
        menuService.delete(id);
        return OperationResult.Success("删除成功",null);
    }
}
