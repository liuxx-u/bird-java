package com.bird.web.sso.ticket;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 票据信息
 *
 * @author liuxx
 * @date 2017/5/17
 */
public class TicketInfo implements Serializable {
    private String userId;
    private String name;
    private Date creationTime;
    private Date lastRefreshTime;
    private Date expireTime;
    private Map<String,Object> claims;

    public TicketInfo() {
        creationTime = new Date();
        lastRefreshTime = new Date();

        claims = new HashMap();
    }

    /**
     * 是否包含claim信息
     * @param key Key
     * @return
     */
    public boolean hasClaim(String key){
        if (StringUtils.isBlank(key)) return false;
        return this.claims.containsKey(key);
    }

    /**
     * 获取票据的claim信息
     * @param key Key
     * @return
     */
    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) return null;
        return this.claims.get(key);
    }

    /**
     * 设置票据的claim信息
     * @param key Key
     * @param value 值
     */
    public void setClaim(String key,Object value) {
        if (StringUtils.isBlank(key) || value == null) return;
        if(hasClaim(key)){
            this.claims.replace(key,value);
        }else {
            this.claims.put(key, value);
        }
    }

    /**
     * 移除票据的claim信息
     * @param key key
     */
    public void removeClaim(String key){
        if(StringUtils.isBlank(key))return;
        this.claims.remove(key);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getClaims() {
        return claims;
    }

    public void setClaims(Map claims) {
        this.claims = claims;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(Date lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
