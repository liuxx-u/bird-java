package com.bird.web.admin.controller;

import com.bird.core.controller.AbstractController;
import com.bird.core.controller.OperationResult;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.TreeDTO;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
@Api(description = "首页接口")
@RestController
@RequestMapping("/home")
public class HomeController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getPaged", method = {RequestMethod.POST})
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, UserDTO.class);
        PagedListResultDTO result = userService.queryPagedList(param);

        return OperationResult.Success("获取成功",result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public OperationResult save(@RequestBody UserDTO dto) {

        CommonSaveParam param = new CommonSaveParam(dto,UserDTO.class);
        userService.save(param);
        return OperationResult.Success("获取成功",null);
    }

    @RequestMapping(value = "/getTreeData", method = {RequestMethod.POST})
    public OperationResult<List<TreeDTO>> getTreeData() {
        List<TreeDTO> list = Arrays.asList(
                new TreeDTO("1","node1"),
                new TreeDTO("2","node2"),
                new TreeDTO("3","node3"),

                new TreeDTO("4","node1-1","1"),
                new TreeDTO("5","node1-2","1"),
                new TreeDTO("6","node2-1","2")
        );
        return OperationResult.Success("获取成功",list);
    }
}
