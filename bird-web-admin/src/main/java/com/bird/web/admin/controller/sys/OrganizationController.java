package com.bird.web.admin.controller.sys;

import com.bird.core.Check;
import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.service.TreeDTO;
import com.bird.service.zero.OrganizationService;
import com.bird.service.zero.dto.OrganizationDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/getTreeData", method = {RequestMethod.GET})
    public OperationResult<List<TreeDTO>> getTreeData() {
        List<TreeDTO> result = organizationService.getOrganizationTreeData();
        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public OperationResult<OrganizationDTO> getMenu(Long id) {
        Check.GreaterThan(id, 0L, "id");
        OrganizationDTO result = organizationService.getOrganization(id);
        return OperationResult.Success("获取成功", result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public OperationResult save(@RequestBody OrganizationDTO dto){
        CommonSaveParam param = new CommonSaveParam(dto, OrganizationDTO.class);
        organizationService.save(param);

        return OperationResult.Success("保存成功", null);
    }
}
