package com.bird.web.sso.ticket;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2019/3/1
 */
@Getter
@Setter
public class TicketInfo implements Serializable{
    private String userId;
    private String tenantId;
    private String userName;
    private String realName;
    private Date creationTime;
    private Date lastRefreshTime;
    private Date expireTime;
    private Map<String, Object> claims;

    public TicketInfo() {
        creationTime = new Date();
        lastRefreshTime = new Date();

        claims = new HashMap<>();
    }

    /**
     * 是否包含claim信息
     *
     * @param key Key
     * @return
     */
    public boolean hasClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return this.claims.containsKey(key);
    }

    /**
     * 获取票据的claim信息
     *
     * @param key Key
     * @return
     */
    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.claims.get(key);
    }

    /**
     * 设置票据的claim信息
     *
     * @param key   Key
     * @param value 值
     */
    public void setClaim(String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            return;
        }
        if (hasClaim(key)) {
            this.claims.replace(key, value);
        } else {
            this.claims.put(key, value);
        }
    }

    /**
     * 移除票据的claim信息
     *
     * @param key key
     */
    public void removeClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        this.claims.remove(key);
    }
}
