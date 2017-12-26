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
import com.bird.service.zero.ModuleService;
import com.bird.service.zero.SiteService;
import com.bird.service.zero.dto.ModuleDTO;
import com.bird.service.zero.dto.SiteDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "系统-站点接口")
@RestController
@RequestMapping("/sys/site")
public class SiteController extends AbstractController {
    @Autowired
    private SiteService siteService;

    @Autowired
    private ModuleService moduleService;

    @ApiOperation("获取站点分页")
    @PostMapping(value = "/getPaged")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, SiteDTO.class);
        PagedListResultDTO result = siteService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("保存站点")
    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody SiteDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, SiteDTO.class);
        siteService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @ApiOperation("删除站点")
    @PostMapping(value = "/delete")
    public OperationResult delete(Long id) {
        siteService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @ApiOperation("获取站点的模块树数据")
    @GetMapping(value = "/getModuleTreeData")
    public OperationResult<List<TreeDTO>> getModuleTreeData(Long siteId) {
        Check.GreaterThan(siteId, 0L, "siteId");
        TreeQueryParam param = new TreeQueryParam("`id`", "`name`", "`parentId`");
        param.setFrom("`zero_module`");
        param.setWhere("siteId=" + siteId);

        List<TreeDTO> result = moduleService.getTreeData(param);

        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("获取模块信息")
    @GetMapping(value = "/getModule")
    public OperationResult<ModuleDTO> getModule(Long id) {
        Check.GreaterThan(id, 0L, "id");
        ModuleDTO result = moduleService.getModule(id);
        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("保存模块")
    @PostMapping(value = "/saveModule")
    public OperationResult save(@RequestBody ModuleDTO dto){
        moduleService.saveModule(dto);

        return OperationResult.Success("保存成功", null);
    }
}
