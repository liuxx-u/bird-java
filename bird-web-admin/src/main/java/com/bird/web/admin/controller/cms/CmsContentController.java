package com.bird.web.admin.controller.cms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.core.controller.OperationResult;
import com.bird.service.cms.CmsContentService;
import com.bird.service.cms.dto.CmsFullContentDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "cms-文章接口")
@RequestMapping("/cms/content")
public class CmsContentController {

    @Reference
    private CmsContentService contentService;

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody CmsFullContentDTO dto){
        contentService.saveContent(dto);

        return OperationResult.Success("保存成功", null);
    }
}
