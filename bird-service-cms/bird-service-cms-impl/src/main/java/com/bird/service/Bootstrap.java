package com.bird.service;

import com.bird.core.cache.CacheHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        //加载spring配置
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring-cms-context.xml"});
        context.start();

        CacheHelper.getCache().set("123","dddd");

        System.out.println("=================================");
        System.out.println("[CMS服务]启动完成!!!");
        System.out.println("=================================");
    }
}
