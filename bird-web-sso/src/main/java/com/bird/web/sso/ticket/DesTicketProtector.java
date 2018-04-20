package com.bird.web.sso.ticket;

import com.alibaba.fastjson.JSON;
import com.bird.core.security.SecurityHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Des票据加密解密器
 *
 * @author liuxx
 * @date 2017/5/18
 */
public class DesTicketProtector implements ITicketProtector {
    private String key = "bode1234";
    private String salt = "http://user.bird.com/";

    @Override
    public String protect(TicketInfo ticket) {
        try {
            byte[] key = this.getKey().getBytes("UTF-8");
            String saltToken = SecurityHelper.encryptDes(salt, key);

            String json = JSON.toJSONString(ticket);
            String token = SecurityHelper.encryptDes(json);
            return token + saltToken;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TicketInfo unProtect(String token) {
        try {
            byte[] key = this.getKey().getBytes("UTF-8");
            String saltToken = SecurityHelper.encryptDes(salt, key);

            if (token.endsWith(saltToken)) {
                String ticketToken = token.substring(0, token.length() - saltToken.length());
                String json = SecurityHelper.decryptDes(ticketToken, key);
                TicketInfo ticketInfo = JSON.parseObject(json, TicketInfo.class);
                return ticketInfo;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
