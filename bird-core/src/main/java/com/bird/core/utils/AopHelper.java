package com.bird.core.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * @author liuxx
 * @date 2019/5/5
 */
public final class AopHelper {

    private AopHelper(){}

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return 目标对象
     */
    public static Object getTarget(Object proxy) {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        try {
            Object target = AopUtils.isJdkDynamicProxy(proxy)
                    ? getJdkDynamicProxyTargetObject(proxy)
                    : getCglibProxyTargetObject(proxy);

            return getTarget(target);
        } catch (Exception ex) {
            return proxy;
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        return getProxyTargetObject(dynamicAdvisedInterceptor);
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        return getProxyTargetObject(aopProxy);
    }

    private static Object getProxyTargetObject(Object proxy) throws Exception{
        Field advised = proxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(proxy)).getTargetSource().getTarget();
    }
}
