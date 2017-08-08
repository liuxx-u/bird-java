package com.cczcrv.core.sso;

import com.alibaba.fastjson.annotation.JSONField;
import com.cczcrv.core.NameValue;

import java.util.Date;
import java.util.List;

/**
 * 票据信息从.net用户中心获取
 * Created by liuxx on 2017/5/17.
 */
public class TicketInfo {
    @JSONField(name = "UserId")
    private String userId;
    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "AuthenticationType")
    private String authenticationType;
    @JSONField(name = "CreationTime",format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date creationTime;
    @JSONField(name = "LastRefreshTime",format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date lastRefreshTime;
    @JSONField(name = "ExpireTime",format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date expireTime;
    @JSONField(name = "Claims")
    private List<NameValue> claims;


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

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
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

    public List<NameValue> getClaims() {
        return claims;
    }

    public void setClaims(List<NameValue> claims) {
        this.claims = claims;
    }
}
