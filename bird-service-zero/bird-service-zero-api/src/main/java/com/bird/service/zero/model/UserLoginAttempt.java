package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractFullModel;

/**
 *  用户登陆日志Model
 *  @author liuweidong
 * */
@TableName("zero_user_login_attempt")
public class UserLoginAttempt extends AbstractFullModel {

    private String userName; // 用户名
    private String clientIp; // 登录客户端IP
    private String clientName; // 登录客户端名称
    private String browserInfo; // 浏览器信息
    private Integer result; // 1.成功;2.用户名不存在;3.密码错误;4.用户被锁定

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
