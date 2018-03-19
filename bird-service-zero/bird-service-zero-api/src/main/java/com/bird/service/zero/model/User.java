package com.bird.service.zero.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bird.service.common.model.AbstractModel;

import java.sql.Date;

/**
 * Created by liuxx on 2017/10/10.
 */

@TableName("zero_user")
public class User extends AbstractModel {
    private String userName;

    private String password;

    private String nickName;

    private String phoneNo;

    private boolean locked;

    private Date lastLoginTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
