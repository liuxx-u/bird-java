package com.bird.web.admin.controller.sys;

import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.TreeDTO;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.DicItemService;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.dto.DicItemDTO;
import com.bird.service.zero.dto.DicTypeDTO;
import com.bird.service.zero.dto.RoleDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuxx on 2017/11/3.
 */
@Api(description = "系统-字典接口")
@RestController
@RequestMapping("/sys/dic")
public class DicController extends AbstractController {

    @Autowired
    private DicTypeService dicTypeService;

    @Autowired
    private DicItemService dicItemService;

    @RequestMapping(value = "/getTreeData", method = {RequestMethod.GET})
    public OperationResult<List<TreeDTO>> getTreeData() {
        List<TreeDTO> result = dicTypeService.getDicTypeTreeData();
        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/getPaged", method = {RequestMethod.POST})
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, DicTypeDTO.class);
        PagedListResultDTO result = dicTypeService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public OperationResult save(@RequestBody DicTypeDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, DicTypeDTO.class);
        dicTypeService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public OperationResult delete(Long id) {
        dicTypeService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @RequestMapping(value = "/getPagedItems", method = {RequestMethod.POST})
    public OperationResult<PagedListResultDTO> getPagedItems(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, DicItemDTO.class);
        PagedListResultDTO result = dicItemService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/saveItem", method = {RequestMethod.POST})
    public OperationResult saveItem(@RequestBody DicItemDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, DicItemDTO.class);
        dicItemService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @RequestMapping(value = "/deleteItem", method = {RequestMethod.POST})
    public OperationResult deleteItem(Long id) {
        dicItemService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }
}
