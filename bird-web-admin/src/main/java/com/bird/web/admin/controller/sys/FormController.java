package com.bird.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.service.common.mapper.CommonSaveParam;
import com.bird.service.common.mapper.PagedQueryParam;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.service.dto.TreeDTO;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import com.bird.service.zero.FieldService;
import com.bird.service.zero.FormService;
import com.bird.service.zero.dto.FieldDTO;
import com.bird.service.zero.dto.FormDTO;
import com.bird.service.zero.dto.FormOperateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "系统-站点接口")
@RestController
@RequestMapping("/sys/form")
public class FormController extends AbstractController {
    @Reference
    private FormService formService;

    @Reference
    private FieldService fieldService;

    @ApiOperation("根据key获取表单模板")
    @GetMapping(value = "/getFormByKey")
    public OperationResult<FormOperateDTO> getFormByKey(String key) {
        FormOperateDTO result = formService.getFormByKey(key);
        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("获取表单树数据")
    @GetMapping(value = "/getTreeData")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`","`name`");
        param.setFrom("`zero_form`");
        List<TreeDTO> result = formService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("获取表单分页")
    @PostMapping(value = "/getPaged")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, FormDTO.class);
        PagedListResultDTO result = formService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("保存表单信息")
    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody FormDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, FormDTO.class);
        formService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @ApiOperation("删除表单信息")
    @PostMapping(value = "/delete")
    public OperationResult delete(Long id) {
        formService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @ApiOperation("获取表单字段分页")
    @PostMapping(value = "/getPagedFields")
    public OperationResult<PagedListResultDTO> getPagedFields(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, FieldDTO.class);
        PagedListResultDTO result = fieldService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @ApiOperation("保存表单字段")
    @PostMapping(value = "/saveField")
    public OperationResult saveField(@RequestBody FieldDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, FieldDTO.class);
        fieldService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @ApiOperation("删除表单字段")
    @PostMapping(value = "/deleteField")
    public OperationResult deleteField(Long id) {
        fieldService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }
}
