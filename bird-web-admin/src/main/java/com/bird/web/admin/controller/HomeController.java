package com.bird.web.admin.controller;

import com.bird.core.controller.AbstractController;
import com.bird.core.mapper.CommonSaveParam;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.FilterRule;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
@Api(description = "首页控制器")
@RestController
@RequestMapping("/test")
public class HomeController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getPaged", method = {RequestMethod.POST})
    public PagedListResultDTO getPaged(@RequestBody PagedListQueryDTO query) {
        PagedQueryParam param = new PagedQueryParam(query, UserDTO.class);
        PagedListResultDTO result = userService.queryPagedList(param);

        return result;
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public void save(@RequestBody UserDTO dto) {
        CommonSaveParam param = new CommonSaveParam(dto,UserDTO.class);
        userService.save(param);
    }
}
