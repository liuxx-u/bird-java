package com.bird.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        //加载spring配置
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring-cms-context.xml"});
        context.start();

        System.out.println("=================================");
        System.out.println("[CMS服务]启动完成!!!");
        System.out.println("=================================");
    }
}
