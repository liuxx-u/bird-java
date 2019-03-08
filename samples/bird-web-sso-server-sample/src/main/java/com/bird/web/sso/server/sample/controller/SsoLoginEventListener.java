package com.bird.web.sso.server.sample.controller;

import com.bird.web.sso.event.ISsoEventListener;
import com.bird.web.sso.server.event.SsoServerLoginEvent;
import org.springframework.stereotype.Component;

/**
 * @author liuxx
 * @date 2019/3/8
 */
@Component
public class SsoLoginEventListener implements ISsoEventListener<SsoServerLoginEvent> {

    @Override
    public void onEvent(SsoServerLoginEvent event) {
        System.out.println(event.toString());
    }
}
