package com.bird.core.scan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.NonNull;

/**
 * @author liuxx
 * @since 2020/5/29
 */
public class ScanExcludeFilterPostProcessor implements PriorityOrdered, BeanDefinitionRegistryPostProcessor {

    private static final String BEAN_NAME = "com.bird.web.common.scan.ScanExcludeFilter";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub

    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        RootBeanDefinition definition = new RootBeanDefinition(ScanExcludeFilter.class);
        registry.registerBeanDefinition(BEAN_NAME, definition);
    }
}
