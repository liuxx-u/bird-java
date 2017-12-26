package com.bird.web.admin.controller.cms;

import com.bird.core.Check;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.cms.CmsAttributeService;
import com.bird.service.cms.dto.CmsAttributeDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "cms-属性接口")
@RestController
@RequestMapping("/cms/attribute")
public class CmsAttributeController {
    @Autowired
    private CmsAttributeService attributeService;

    @PostMapping(value = "/getPaged")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, CmsAttributeDTO.class);
        PagedListResultDTO result = attributeService.queryPagedList(param);

        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody CmsAttributeDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto, CmsAttributeDTO.class);
        attributeService.save(param);

        return OperationResult.Success("保存成功", null);
    }

    @PostMapping(value = "/delete")
    public OperationResult delete(Long id) {
        attributeService.softDelete(id);

        return OperationResult.Success("删除成功", null);
    }

    @GetMapping(value = "/getClassifyAttribute")
    public OperationResult<List<CmsAttributeDTO>> getClassifyAttribute(Long classifyId) {
        Check.GreaterThan(classifyId, 0L, "classifyId");
        List<CmsAttributeDTO> result = attributeService.getClassifyAttribute(classifyId);
        return OperationResult.Success("获取成功", result);
    }
}
