package com.bird.service;

import com.bird.core.NameValue;
import com.bird.core.utils.CollectionWrapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuxx on 2017/8/2.
 */
public class Bootstrap {
    public static void main(String arg[]){
        //加载spring配置
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath:spring-zero-context.xml"});
        context.start();

        List<Integer> testList = Arrays.asList(1,2,3,4,5);
        List result = CollectionWrapper.init(testList).where(p->p>3).toList();
        List result2 = CollectionWrapper.init(testList).where(p->p>3).select(p->new NameValue(p.toString(),p.toString())).toList();

        System.out.println("=================================");
        System.out.println("[ZERO服务]启动完成!!!");
        System.out.println("=================================");
    }
}
