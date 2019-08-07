package com.bird.trace.client.sample;

import com.bird.trace.client.aspect.Traceable;
//import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxx
 * @date 2019/8/6
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    @Traceable(value = "trace-controller")
    public String test() {
        testService.test("liuxx", "hahaha");
        return "test";
    }
//    ClientPreparedStatement
}
