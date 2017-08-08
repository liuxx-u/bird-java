package com.cczcrv.service;

import com.cczcrv.core.utils.DataHelper;
import com.cczcrv.service.cms.ContentService;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        PropertyConfigurator.configure(DataHelper.getFullPathRelateClass("../../../config/log4j.properties",Bootstrap.class));
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring/spring-order-context.xml"});
        context.start();

        ContentService contentService=context.getBean(ContentService.class);
        System.out.println("=================================");
        System.out.println("[订单服务]启动完成!!!");
        System.out.println("=================================");
    }
}
