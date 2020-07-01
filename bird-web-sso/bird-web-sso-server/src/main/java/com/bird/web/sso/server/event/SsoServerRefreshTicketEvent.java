package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.ServerTicket;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户票据刷新时触发的事件
 *
 * @author liuxx
 * @date 2019/3/7
 */
@Getter
@Setter
public class SsoServerRefreshTicketEvent extends SsoEvent {

    /**
     * 是否自动刷新
     */
    private Boolean autoRefresh;
    /**
     * 当前的票据信息
     */
    private ServerTicket currentTicket;
    /**
     * 新的票据信息
     */
    private ServerTicket newTicket;

    public SsoServerRefreshTicketEvent(String token, ServerTicket currentTicket, Boolean autoRefresh, ServerTicket newTicket) {
        super(token);
        this.autoRefresh = autoRefresh;

        this.currentTicket = currentTicket;
        this.newTicket = newTicket;
    }
}
