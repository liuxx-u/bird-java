package com.bird.web.sso.event;

import lombok.Data;

import java.util.Date;

/**
 * @author liuxx
 * @date 2019/3/7
 */
@Data
public abstract class SsoEvent {
    private String token;
    private Date eventTime;

    public SsoEvent(){
        this.eventTime = new Date();
    }

    public SsoEvent(String token){
        this.token = token;
        this.eventTime = new Date();
    }
}
