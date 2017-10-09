package com.bird.service;

import com.bird.core.scheduler.QuartzManager;
import com.bird.scheduler.TestJob;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        //加载spring配置
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring/spring-cms-context.xml"});
        context.start();

        //加载定时任务
        QuartzManager quartzManager=context.getBean(QuartzManager.class);
        quartzManager.addJob(TestJob.class,"0 0/1 * * * ?");

        System.out.println("=================================");
        System.out.println("[新闻服务]启动完成!!!");
        System.out.println("=================================");
    }
}
