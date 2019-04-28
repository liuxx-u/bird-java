package com.bird.service.cms;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.fastjson.JSON;
import com.bird.core.initialize.InitializeExecutor;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.common.mapper.QueryDescriptor;
import com.bird.service.common.service.query.FilterGroup;
import com.bird.service.common.service.query.FilterOperate;
import com.bird.service.common.service.query.FilterRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.bird")
@MapperScan("com.bird.service.cms.mapper")
@DubboComponentScan(basePackages = "com.bird.service.cms")
public class Bootstrap {

	public static void main(String[] args) {
		SpringApplication.run(Bootstrap.class, args);

		System.out.println("=================================");
		System.out.println("[CMS服务]启动完成!!!");
		System.out.println("=================================");

		List<FilterRule> rules = new ArrayList<>();
		rules.add(new FilterRule("id", FilterOperate.IN,"3,59"));
		rules.add(new FilterRule("name",FilterOperate.CONTAINS,"2"));

		FilterGroup group = new FilterGroup(rules);
		List<FilterGroup> groups1 = new ArrayList<>();
		List<FilterGroup> groups2 = new ArrayList<>();

		groups1.add(new FilterGroup(rules));
		groups2.add(new FilterGroup(rules));

		FilterGroup group1 = new FilterGroup(rules);
		group1.or(groups2);

		groups1.add(group1);
		group.and(groups1);

		System.out.println(QueryDescriptor.parseClass(CmsClassifyDTO.class).formatFilters(group));
	}
}
