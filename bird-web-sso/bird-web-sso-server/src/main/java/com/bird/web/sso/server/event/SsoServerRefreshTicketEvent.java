package com.bird.web.sso.server.event;

import com.bird.web.sso.event.SsoEvent;
import com.bird.web.sso.ticket.TicketInfo;
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
     * 新的票据信息
     */
    private TicketInfo newTicketInfo;

    public SsoServerRefreshTicketEvent(String token, TicketInfo ticketInfo, Boolean autoRefresh, TicketInfo newTicketInfo) {
        super(token, ticketInfo);
        this.autoRefresh = autoRefresh;
        this.newTicketInfo = newTicketInfo;
    }
}
