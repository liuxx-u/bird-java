package com.bird.service.sample;

import com.bird.service.common.incrementer.UUIDHexGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@Configuration
public class Config {

    /**
     * 注入String类型主键生成器
     * @return 有序的UUID主键生成器
     */
    @Bean
    public UUIDHexGenerator uuidHexGenerator(){
        return new UUIDHexGenerator();
    }
}
