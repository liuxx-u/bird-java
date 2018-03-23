package com.bird.web.api;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liuxx
 * @date 2018/3/20
 */
@SpringBootApplication
@ComponentScan("com.bird")
@DubboComponentScan("com.bird.web.api.controller")
@EnableSwagger2
public class WebApplication {

    public static void main(String arg[]){
        SpringApplication.run(WebApplication.class, arg);
    }
}
