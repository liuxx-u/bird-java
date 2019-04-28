package com.bird.gateway.bootstrap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootstrapTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void test(){
		String path = "/thctay-service-zero/sub1/sub2";
		String subPath = path.substring(path.indexOf(19+1));
	}
}
