package com.bird.core.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author liuxx
 * @date 2019/5/5
 */
public final class AopHelper {

    private AopHelper() {
    }

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return 目标对象
     */
    public static Object getTarget(Object proxy) {
        try {
            if (isJdkDynamicProxy(proxy)) {
                return getTarget(getJdkDynamicProxyTargetObject(proxy));
            } else if (isCglibProxy(proxy)) {
                return getTarget(getCglibProxyTargetObject(proxy));
            } else {
                return proxy;
            }
        } catch (Exception ex) {
            return proxy;
        }
    }

    private static boolean isJdkDynamicProxy(@Nullable Object object) {
        return object != null && Proxy.isProxyClass(object.getClass());
    }

    private static boolean isCglibProxy(@Nullable Object object) {
        return object != null && object.getClass().getName().contains("$$");
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
        Object aopProxy = h.get(proxy);
        return getProxyTargetObject(aopProxy);
    }

    private static Object getProxyTargetObject(Object proxy) {
        try {
            Field advised = proxy.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport) advised.get(proxy)).getTargetSource().getTarget();
        } catch (Exception ex) {
            return proxy;
        }
    }
}
