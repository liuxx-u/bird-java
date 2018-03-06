package com.bird.web.admin.controller.cms;

import com.bird.core.Check;
import com.bird.core.controller.OperationResult;
import com.bird.service.cms.CmsClassifyService;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.common.mapper.TreeQueryParam;
import com.bird.service.common.service.dto.TreeDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "cms-分类接口")
@RestController
@RequestMapping("/cms/classify")
public class CmsClassifyController {
    @Autowired
    private CmsClassifyService classifyService;

    @GetMapping(value = "/getTreeData")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`", "`name`", "`parentId`");
        param.setFrom("`cms_classify`");

        List<TreeDTO> result = classifyService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/get")
    public OperationResult<CmsClassifyDTO> get(Long id) {
        Check.GreaterThan(id, 0L, "id");
        CmsClassifyDTO result = classifyService.getClassify(id);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody CmsClassifyDTO dto){
        classifyService.saveClassify(dto);

        return OperationResult.Success("保存成功", null);
    }
}
