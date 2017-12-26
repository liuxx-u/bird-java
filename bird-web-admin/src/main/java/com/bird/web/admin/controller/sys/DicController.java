package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.mapper.TreeQueryParam;
import com.bird.core.service.TreeDTO;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.DicItemService;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/getTreeData")
    @ApiOperation(value = "获取字典树数据")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`", "`name`","`parentId`");
        param.setFrom("`zero_dictype`");
        List<TreeDTO> result = dicTypeService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/getDicByKey")
    @ApiOperation(value = "根据key获取字典信息")
    public OperationResult<DicDTO> getDicByKey(String key) {
        Check.NotEmpty(key, "key");
        DicDTO result = dicTypeService.getDicByKey(key);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/get")
    @ApiOperation(value = "获取字典")
    public OperationResult<DicTypeDTO> get(Long id) {
        Check.GreaterThan(id, 0L, "id");
        DicTypeDTO result = dicTypeService.getDicType(id);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存字典")
    public OperationResult save(@RequestBody DicTypeDTO dto) {
        Check.NotNull(dto, "dto");
        if (dto.getParentId() == null) {
            dto.setParentId(0L);
        }
        CommonSaveParam param = new CommonSaveParam(dto, DicTypeDTO.class);
        dicTypeService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除字典")
    public OperationResult delete(Long id) {
        dicTypeService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @PostMapping(value = "/getPagedItems")
    @ApiOperation(value = "获取字典选项列表")
    public OperationResult<PagedListResultDTO> getPagedItems(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, DicItemDTO.class);
        PagedListResultDTO result = dicItemService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/saveItem")
    @ApiOperation(value = "保存字典选项")
    public OperationResult saveItem(@RequestBody DicItemDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, DicItemDTO.class);
        dicItemService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/deleteItem")
    @ApiOperation(value = "删除字典选项")
    public OperationResult deleteItem(Long id) {
        dicItemService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }
}
