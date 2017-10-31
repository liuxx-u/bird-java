package com.bird.security;

import com.bird.service.zero.model.User;

import java.io.Serializable;

/**
 * 用于token生成的用户信息。
 * Created by liuxx on 2017/10/24.
 */
public class Principal implements Serializable {
    private Long id;
    private String userName;

    public Principal(){}

    public Principal(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
