package com.bird.web.common.scan;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

/**
 * @author liuxx
 * @since 2020/5/29
 */
public class ScanExcludeFilterContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new ScanExcludeFilterPostProcessor());
    }
}
