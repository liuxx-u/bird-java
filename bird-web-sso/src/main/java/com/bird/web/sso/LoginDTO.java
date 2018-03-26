package com.bird.web.sso;

import java.io.Serializable;

public class LoginDTO implements Serializable {
    private String userName;
    private String password;

    public LoginDTO(){}
    public LoginDTO(String userName,String password) {
        this.userName = userName;
        this.password = password;
    }


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
}
