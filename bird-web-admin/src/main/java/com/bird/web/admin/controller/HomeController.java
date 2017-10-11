package com.bird.web.admin.controller;

import com.bird.core.controller.AbstractController;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.FilterRule;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.service.query.PagedListResultDTO;
import com.bird.service.zero.UserService;
import com.bird.service.zero.dto.UserDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxx on 2017/6/27.
 */
@Api(description = "首页控制器")
@RestController
@RequestMapping("/")
public class HomeController extends AbstractController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/home", method = {RequestMethod.POST})
    public PagedListResultDTO Index() {
        PagedListQueryDTO query = new PagedListQueryDTO();
        query.setPageIndex(1);
        query.setPageSize(10);

        List<FilterRule> rules=new ArrayList<>();
        rules.add(new FilterRule("userName", "liuxx"));
        query.setFilters(rules);

        PagedQueryParam param = new PagedQueryParam(query, UserDTO.class);
        PagedListResultDTO result= userService.queryPagedList(param);

        return result;
    }
}
