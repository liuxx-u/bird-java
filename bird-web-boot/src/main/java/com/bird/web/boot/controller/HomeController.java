package com.bird.web.boot.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.service.zero.DicTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxx
 * @date 2018/3/19
 */
@RestController
@RequestMapping("/dic")
public class HomeController {

    @Reference
    private DicTypeService dicTypeService;

    @RequestMapping("/test")
    public String test(){
        return "sas";
    }
}
