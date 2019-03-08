package com.bird.web.sso.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxx
 * @date 2019/3/5
 */
@RestController
@RequestMapping("/sso/server/account")
public class AccountController {

    /**
     * 登录
     * @return token
     */
    public String login(){
        return StringUtils.EMPTY;
    }

    /**
     * 注销
     */
    public void logout(){

    }
}
