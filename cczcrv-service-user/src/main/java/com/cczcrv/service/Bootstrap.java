package com.cczcrv.service;

import com.cczcrv.core.utils.DataHelper;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        PropertyConfigurator.configure(DataHelper.getFullPathRelateClass("../../../config/log4j.properties",Bootstrap.class));
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring/spring-user-context.xml"});
        context.start();

        System.out.println("=================================");
        System.out.println("[用户服务]启动完成!!!");
        System.out.println("=================================");
    }
}
