package com.bird.web.sso.ticket;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端票据信息
 *
 * @author liuxx
 * @since 2020/6/30
 */
@Data
public class ClientTicket {
    /**
     * userId
     */
    private String userId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * userName
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 客户端票据创建时间
     */
    private Date createTime;
    /**
     * 摘要信息
     */
    private Map<String, Object> claims;

    public ClientTicket(){
        this.createTime = new Date();
        this.claims = new HashMap<>();
    }

    /**
     * 是否包含claim信息
     *
     * @param key Key
     * @return bool
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
     * @return claim
     */
    public Object getClaim(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.claims.get(key);
    }
}
