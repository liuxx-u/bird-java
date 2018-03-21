package com.bird.service.zero;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.bird.core.utils.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bird")
@MapperScan("com.bird.service.zero.mapper")
@DubboComponentScan(basePackages = "com.bird.service.zero")
public class Bootstrap {

	public static void main(String[] args) {
		SpringApplication.run(Bootstrap.class, args);

		OrganizationService organizationService = SpringContextHolder.getBean(OrganizationService.class);
		organizationService.getOrganization(1L);
	}
}
