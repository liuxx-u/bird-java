package com.bird.core.aspect.operate;

import com.alibaba.fastjson.JSON;
import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * @author liuxx
 * @date 2019/1/15
 */
@Data
public class OperateLogInfo implements Serializable {
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
     * 记录时间
     */
    private Date createTime;

    public OperateLogInfo() {
        BirdSession session = SessionContext.getSession();
        if (session != null && session.getUserId() != null) {
            this.userId = session.getUserId().toString();
            this.userName = session.getName();
        }
        this.createTime = new Date();
    }

    public OperateLogInfo(Method method, Object[] args) {
        this();
        if (method == null) return;

        OperateLog annotation = method.getAnnotation(OperateLog.class);
        this.operation = annotation != null ? annotation.value() : StringUtils.EMPTY;

        this.method = method.getName();
        this.clazz = method.getDeclaringClass().getName();

        this.params = args != null ? JSON.toJSONString(args) : StringUtils.EMPTY;
    }
}
