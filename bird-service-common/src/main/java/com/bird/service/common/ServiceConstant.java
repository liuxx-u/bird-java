package com.bird.service.common;

/**
 * @author liuxx
 * @date 2018/3/8
 *
 * 服务相关的公共常量
 */
public interface ServiceConstant {

    /**
     * 软删除状态
     */
    interface SoftDelete {
        Byte TRUE = 1;
        Byte FALSE = 0;
    }

    /**
     * 数据权限常量
     */
    interface DataPermission{
        /**
         * 数据权限筛选的数据库字段
         */
        String DEFAULT_AUTHORITY_FIELD = "userId";
        /**
         * 数据权限数据参数名（mybatis中的参数名）
         */
        String AUTHORITY_PARAM_NAME = "authority";

        /**
         * 数据权限缓存Key前缀
         */
        String CACHE_KEY = "data_permission:bird:";
    }

    interface Page{
        /**
         * 每页最大获取数量
         */
        int MAX_PAGESIZE = 200;

        /**
         * 默认每页显示数量
         */
        int DEFAULT_PAGESIZE = 15;
    }
}
