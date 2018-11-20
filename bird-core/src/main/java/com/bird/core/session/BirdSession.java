package com.bird.core.session;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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
}
