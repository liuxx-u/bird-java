package com.cczcrv.website.controller;

import com.cczcrv.core.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuxx on 2017/6/27.
 */
@Api(description = "测试控制器")
@RestController
@RequestMapping("/test")
public class TestController extends AbstractController {

    @ApiOperation("首页")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String Index() {
        return "welcome to liuxx's home";
    }
}
