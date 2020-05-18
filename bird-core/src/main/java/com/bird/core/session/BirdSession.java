package com.bird.core.session;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/5/11
 *
 * session的抽象,用于统一管理session信息
 */
@Data
public class BirdSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * userId
     */
    private Serializable userId;
    /**
     * 租户id
     */
    private Serializable tenantId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 摘要信息集合
     */
    private Map<String,Object> claims;
    /**
     * 创建时间
     */
    private Date createTime;

    public BirdSession(){
        claims = new HashMap<>();
    }

    /**
     * 是否包含claim信息
     * @param key Key
     * @return true or false
     */
    public boolean hasClaim(String key){
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return this.claims.containsKey(key);
    }

    /**
     * 获取claim信息
     * @param key Key
     * @return claim
     */
    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.claims.get(key);
    }
}
