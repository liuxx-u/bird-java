package com.bird.trace.client;

import com.alibaba.fastjson.JSON;
import com.bird.trace.client.aspect.Traceable;
import com.bird.trace.client.sql.TraceSQL;
import com.bird.trace.client.sql.TraceSQLType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

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
     * 返回值
     */
    private String returnValue;
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
     * 记录SQL语句的类型
     */
    private List<TraceSQLType> sqlTypes;
    /**
     * 扩展信息
     */
    private Map<String, Object> ext;

    public TraceLog() {
        this.startTime = new Date();
    }

    public TraceLog(Method method, Object[] args,List<TraceSQLType> sqlTypes) {
        this();
        if (method == null) {
            return;
        }

        this.sqlTypes = sqlTypes;
        this.operation = StringUtils.EMPTY;

        this.method = method.getName();
        this.clazz = method.getDeclaringClass().getName();
        this.params = args != null ? JSON.toJSONString(args) : StringUtils.EMPTY;

        Traceable annotation = method.getAnnotation(Traceable.class);
        if(annotation != null) {
            this.operation = annotation.value();
            if (annotation.sqlTypes().length > 0) {
                this.sqlTypes = Arrays.asList(annotation.sqlTypes());
            }
        }
    }

    /**
     * 附加SQL记录
     * @param sql sql
     */
    public void appendSQL(TraceSQL sql){
        if(this.sqls == null || this.sqls.isEmpty()){
            this.sqls = new ArrayList<>();
        }
        this.sqls.add(sql);
    }
}
