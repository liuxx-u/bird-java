package com.bird.trace.client;

import com.bird.trace.client.sql.TraceSQL;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/8/4
 */
@Data
public class TraceLog {
    /**
     * Key
     */
    private String key;
    /**
     * 父级key
     */
    private String parentKey;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 操作
     */
    private String operation;
    /**
     * 类名
     */
    private String clazz;
    /**
     * 方法名
     */
    private String method;
    /**
     * 参数
     */
    private String params;
    /**
     * 记录开始时间
     */
    private Date startTime;
    /**
     * 记录结束时间
     */
    private Date endTime;
    /**
     * sql语句信息
     */
    private List<TraceSQL> sqls;
    /**
     * 扩展信息
     */
    private Map<String,Object> ext;
}
