package com.cczcrv.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring/spring-product-context.xml"});
        context.start();

        System.out.println("=================================");
        System.out.println("[商品服务]启动完成!!!");
        System.out.println("=================================");
    }
}
