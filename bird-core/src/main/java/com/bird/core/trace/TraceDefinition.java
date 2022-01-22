package com.bird.core.trace;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author liuxx
 * @since 2020/10/9
 */
@Data
public class TraceDefinition implements Serializable {

    private static final long serialVersionUID = 2405172041920251807L;

    /**
     * 全局轨迹id
     */
    private String globalTraceId;
    /**
     * 轨迹id
     */
    private String traceId;
    /**
     * 父级轨迹id
     */
    private String parentTraceId;
    /**
     * 轨迹描述
     */
    private String description;
    /**
     * 标签集合
     */
    private List<String> tags;
    /**
     * 请求入口
     */
    private String entrance;
    /**
     * 请求参数
     */
    private Object[] params;
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 记录开始时间
     */
    private Long startTime;
    /**
     * 记录结束时间
     */
    private Long endTime;
    /**
     * 摘要扩展信息集合
     */
    private Map<String, Object> claims;

    public TraceDefinition() {
        this.claims = new HashMap<>();
        this.tags = new ArrayList<>();
    }

    public TraceDefinition next(String entrance, Object[] params) {
        TraceDefinition next = initWithDefault(entrance, params);
        next.globalTraceId = this.globalTraceId;
        next.parentTraceId = this.traceId;
        return next;
    }

    public static TraceDefinition initWithDefault(String entrance, Object[] params) {
        TraceDefinition definition = new TraceDefinition();
        definition.traceId = UUID.randomUUID().toString();
        definition.startTime = System.currentTimeMillis();
        definition.entrance = entrance;
        definition.params = params;

        BirdSession session = SessionContext.getSession();
        if (session != null && session.getUserId() != null) {
            definition.userId = session.getUserId().toString();
            definition.userName = StringUtils.isNotBlank(session.getRealName())
                    ? session.getRealName()
                    : session.getUserName();
        }
        return definition;
    }

    public void addTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            return;
        }
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }

    public boolean hasTag(String tag) {
        if (StringUtils.isBlank(tag)) {
            return false;
        }
        return this.tags.contains(tag);
    }

    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.claims.get(key);
    }

    public void setClaim(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        this.claims.put(key, value);
    }
}
