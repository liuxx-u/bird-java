package com.bird.web.sso.ticket;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端票据信息
 *
 * @author liuxx
 * @since 2020/6/30
 */
@Data
public class ServerTicket {

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
     * 票据创建时间
     */
    private Date creationTime;
    /**
     * 票据最后刷新时间
     */
    private Date lastRefreshTime;
    /**
     * 票据过期时间
     */
    private Date expireTime;
    /**
     * app 自定义摘要集合
     */
    private Map<String, Map<String, Object>> appClaims;

    public ServerTicket() {
        this.creationTime = new Date();
        this.lastRefreshTime = new Date();

        this.appClaims = new HashMap<>();
    }

    /**
     * 获取客户端票据
     *
     * @param appId 应用id
     * @return 客户端票据
     */
    public ClientTicket extractClientTicket(String appId) {
        ClientTicket clientTicket = new ClientTicket();
        clientTicket.setUserId(this.userId);
        clientTicket.setTenantId(this.tenantId);
        clientTicket.setUserName(this.userName);
        clientTicket.setRealName(this.realName);
        clientTicket.setCreateTime(new Date());
        if (this.appClaims != null) {
            clientTicket.setClaims(this.appClaims.getOrDefault(appId, new HashMap<>(0)));
        }
        return clientTicket;
    }
}
