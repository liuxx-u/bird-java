package com.bird.core.session;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * session的抽象,用于统一web层与dubbo服务层的session信息
 */
public class BirdSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private Serializable userId;
    private Serializable tenantId;
    private String name;
    private Map<String,Object> claims;

    public BirdSession(){
        claims = new HashMap<>();
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
     * 获取claim信息
     * @param key Key
     * @return
     */
    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) return null;
        return this.claims.get(key);
    }

    public Serializable getUserId() {
        return userId;
    }

    public void setUserId(Serializable userId) {
        this.userId = userId;
    }

    public Serializable getTenantId() {
        return tenantId;
    }

    public void setTenantId(Serializable tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }
}
