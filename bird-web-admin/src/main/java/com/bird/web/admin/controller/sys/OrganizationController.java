package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.TreeQueryParam;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.OrganizationService;
import com.bird.service.zero.dto.OrganizationDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liuxx on 2017/11/1.
 */

@Api(description = "系统-组织机构接口")
@RestController
@RequestMapping("/sys/organization")
public class OrganizationController extends AbstractController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping(value = "/getTreeData")
    public OperationResult<List<TreeDTO>> getTreeData() {
        TreeQueryParam param = new TreeQueryParam("`id`","`name`","`parentId`");
        param.setFrom("`zero_organization`");
        List<TreeDTO> result = organizationService.getTreeData(param);
        return OperationResult.Success("获取成功", result);
    }

    @GetMapping(value = "/get")
    public OperationResult<OrganizationDTO> getOrganization(Long id) {
        Check.GreaterThan(id, 0L, "id");
        OrganizationDTO result = organizationService.getOrganization(id);
        return OperationResult.Success("获取成功", result);
    }

    @PostMapping(value = "/save")
    public OperationResult save(@RequestBody OrganizationDTO dto){
        organizationService.saveOrganization(dto);

        return OperationResult.Success("保存成功", null);
    }
}
