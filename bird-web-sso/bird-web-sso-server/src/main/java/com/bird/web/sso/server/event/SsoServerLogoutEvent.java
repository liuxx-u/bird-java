package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.ServerTicket;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户从SsoServer注销时触发的事件
 *
 * @author liuxx
 * @date 2019/3/7
 */
@Getter
@Setter
public class SsoServerLogoutEvent extends SsoEvent {

    /**
     * 票据信息
     */
    private ServerTicket serverTicket;

    public SsoServerLogoutEvent(String token, ServerTicket serverTicket){
        super(token);

        this.serverTicket = serverTicket;
    }
}
