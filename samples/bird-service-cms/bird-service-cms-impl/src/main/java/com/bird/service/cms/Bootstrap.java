package com.bird.service.cms;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bird")
@MapperScan("com.bird.service.cms.mapper")
@DubboComponentScan(basePackages = "com.bird.service.cms")
public class Bootstrap {

	public static void main(String[] args) {
		SpringApplication.run(Bootstrap.class, args);

		System.out.println("=================================");
		System.out.println("[CMS服务]启动完成!!!");
		System.out.println("=================================");
	}
}
