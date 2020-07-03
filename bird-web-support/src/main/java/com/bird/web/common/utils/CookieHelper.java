package com.bird.web.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/4/16
 */
public final class CookieHelper {
    private static final int COOKIE_HALF_HOUR = 30 * 60;

    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * @param request 请求
     * @param name cookie 名
     * @return Cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                cookie = c;
                break;
            }
        }
        return cookie;
    }

    /**
     * 根据Cookie名称直接得到Cookie值
     *
     * @param request 请求
     * @param name cookie 名
     * @return cookie 值
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 移除cookie
     * @param request 请求
     * @param response 响应
     * @param name 这个是名称，不是值
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        Cookie cookie = getCookie(request, name);
        if(cookie != null){
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     *
     * @param response
     * @param name
     * @param value
     * @param maxValue
     */
    public static void setCookie(HttpServletResponse response, String name,String value, int maxValue) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        if (value == null) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxValue != 0) {
            cookie.setMaxAge(maxValue);
        } else {
            cookie.setMaxAge(COOKIE_HALF_HOUR);
        }
        response.addCookie(cookie);
    }

    /**
     * 添加一条新的Cookie，默认30分钟过期时间
     *
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, COOKIE_HALF_HOUR);
    }

    /**
     * 将cookie封装到Map里面
     * @param request 请求
     * @return cookie map
     */
    public static Map<String,Cookie> getCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if(!ArrayUtils.isEmpty(cookies)){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
