package com.bird.core;


import com.bird.core.utils.InstanceHelper;

import java.util.Map;

/**
 * Created by liuxx on 2017/5/16.
 */
public interface Constants {
    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";
    /** 缓存键值 */
    Map<Class<?>, String> cacheKeyMap = InstanceHelper.newHashMap();
    /** 操作名称 */
    String OPERATION_NAME = "OPERATION_NAME";
    /** 客户端语言 */
    String USERLANGUAGE = "userLanguage";
    /** 客户端主题 */
    String WEBTHEME = "webTheme";
    /** 当前用户 */
    String CURRENT_USER = "CURRENT_USER";
    /** 上次请求地址 */
    String PREREQUEST = "PREREQUEST";
    /** 上次请求时间 */
    String PREREQUEST_TIME = "PREREQUEST_TIME";
    /** 登录地址 */
    String LOGIN_URL = "/login.html";
    /** 非法请求次数 */
    String MALICIOUS_REQUEST_TIMES = "MALICIOUS_REQUEST_TIMES";
    /** 缓存命名空间 */
    String CACHE_NAMESPACE = "bird:";
    /** 在线用户数量 */
    String ALLUSER_NUMBER = "SYSTEM:" + CACHE_NAMESPACE + "ALLUSER_NUMBER";
    /** TOKEN */
    String TOKEN_KEY = CACHE_NAMESPACE + "TOKEN_KEY";

    /** 日志表状态 */
    interface JOBSTATE {
        /**
         * 日志表状态，初始状态，插入
         */
        String INIT_STATS = "I";
        /**
         * 日志表状态，成功
         */
        String SUCCESS_STATS = "S";
        /**
         * 日志表状态，失败
         */
        String ERROR_STATS = "E";
        /**
         * 日志表状态，未执行
         */
        String UN_STATS = "N";
    }

    interface HttpMessage {
        String READ_TIME_OUT = "连接超时";
        String CONN_TIME_OUT = "连接超时";
        String REQUEST_FAIL = "请求失败";
        String SYSTEM_ERROR = "系统错误";
    }

    interface SoftDelete {
        int NORMAL = 0; //正常
        int DELETE = 1; //删除
    }

    interface DataAuthority{
        /**
         * 数据权限筛选的数据库字段
         */
        String DEFAULT_AUTHORITY_FIELD = "userId";
        /**
         * 数据权限数据参数名（mybatis中的参数名）
         */
        String AUTHORITY_PARAM_NAME = "authority";

        /**
         * 数据权限缓存Key前缀（mybatis中的参数名）
         */
        String CACHE_KEY = "data_authority:bird:";
    }
}
