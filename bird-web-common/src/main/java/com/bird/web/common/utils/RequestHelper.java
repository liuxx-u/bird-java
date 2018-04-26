package com.bird.web.common.utils;

import com.bird.web.common.WebConstant;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author liuxx
 * @date 2018/4/18
 */
public class RequestHelper {

    /**
     * 获取请求的真实ip地址
     * 请求头可能出现伪造的情况，该方法不能保证获取到的ip是真实的客户机ip
     *
     * X-Forwarded-For：格式为X-Forwarded-For: client1, proxy1, proxy2，一般情况下，第一个ip为客户端真实ip，后面的为经过的代理服务器ip。
     * Proxy-Client-IP/WL- Proxy-Client-IP：用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL- Proxy-Client-IP是他的weblogic插件加上的头
     * HTTP_CLIENT_IP：有些代理服务器会加上此请求头
     * X-Real-IP：nginx代理一般会加上此请求头
     *
     * @param request 请求
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isEffectiveIp(ip) && ip.indexOf(",") > -1) {
            String[] array = ip.split(",");
            for (String str : array) {
                if (isEffectiveIp(str)) {
                    ip = str;
                    break;
                }
            }
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!isEffectiveIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 是否是有效的ip地址
     * @param ip ip地址
     * @return
     */
    private static boolean isEffectiveIp(String ip) {
        if (StringUtils.isBlank(ip) || WebConstant.UNKNOWN_IP.equals(ip)) return false;

        return true;
    }

    /**
     * 是否是Multipart请求
     * @param request 请求
     * @return
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase(Locale.ENGLISH).startsWith(WebConstant.MULTIPART)) {
            return true;
        }
        return false;
    }

}
