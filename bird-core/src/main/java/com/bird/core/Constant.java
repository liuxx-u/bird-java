package com.bird.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/3/8
 *
 * 系统相关的公共常量
 */
public interface Constant {
    /**
     * 异常信息统一头信息
     */
    String EXCEPTION_HEAD = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";

    interface Cache{

        /**
         * 类名与缓存键值Hash表
         */
        Map<Class<?>, String> CLASSKEY_MAP = new HashMap<>();

        /** 缓存命名空间 */
        String NAMESPACE = "bird:";
    }
}
