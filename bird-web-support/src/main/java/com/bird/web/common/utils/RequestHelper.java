package com.bird.web.common.utils;

import com.bird.web.common.WebConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author liuxx
 * @date 2018/4/18
 */
public class RequestHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestHelper.class);

    /**
     * 获取请求的真实ip地址
     * 请求头可能出现伪造的情况，该方法不能保证获取到的ip是真实的客户机ip
     * <p>
     * X-Forwarded-For：格式为X-Forwarded-For: client1, proxy1, proxy2，一般情况下，第一个ip为客户端真实ip，后面的为经过的代理服务器ip。
     * Proxy-Client-IP/WL- Proxy-Client-IP：用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL- Proxy-Client-IP是他的weblogic插件加上的头
     * HTTP_CLIENT_IP：有些代理服务器会加上此请求头
     * X-Real-IP：nginx代理一般会加上此请求头
     *
     * @param request 请求
     * @return ip
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isEffectiveIp(ip) && ip.contains(",")) {
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
     *
     * @param ip ip地址
     * @return is effective
     */
    private static boolean isEffectiveIp(String ip) {
        return !StringUtils.isBlank(ip) && !WebConstant.UNKNOWN_IP.equals(ip);
    }

    /**
     * 是否是Multipart请求
     *
     * @param request 请求
     * @return is multipart
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.toLowerCase(Locale.ENGLISH).startsWith(WebConstant.MULTIPART);
    }

    /**
     * 获取请求Body
     *
     * @param request 请求
     * @return string
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();

        try (
                InputStream inputStream = request.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 获取Content-Length
     * @param request 请求
     * @return content-length
     */
    public static long getContentLength(HttpServletRequest request){
        long size;
        try {
            size = Long.parseLong(request.getHeader(HttpHeaders.CONTENT_LENGTH));
        } catch (NumberFormatException e) {
            size = request.getContentLengthLong();
        }
        return size;
    }
}
