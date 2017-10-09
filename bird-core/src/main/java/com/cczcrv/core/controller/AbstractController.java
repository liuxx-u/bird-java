package com.bird.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bird.core.Constants;
import com.bird.core.HttpCode;
import com.bird.core.exception.AbstractException;
import com.bird.core.sso.TicketHandler;
import com.bird.core.sso.TicketInfo;
import com.bird.core.utils.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuxx on 2017/5/25.
 */
public abstract class AbstractController {
    @Autowired
    protected TicketHandler ticketHandler;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** 获取当前用户Id */
    protected TicketInfo getCurrUser(HttpServletRequest request) {
        return ticketHandler.getTicket(request);
    }

    protected Long getCurrentUserId(HttpServletRequest request) {
        TicketInfo ticketInfo = getCurrUser(request);
        if (ticketInfo == null) return 0l;
        if (StringHelper.isNullOrWhiteSpace(ticketInfo.getUserId())) return 0l;
        return Long.valueOf(ticketInfo.getUserId());
    }

    protected String getToken(HttpServletRequest request){
        String cookieName = "sso.token";
        //先从header中获取token
        String token = request.getHeader(cookieName);
        if (!StringHelper.isNullOrWhiteSpace(token)) {
            return token;
        }
        //header中没有token,则从cookie中获取
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /** 异常处理 */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws Exception {
        logger.error(Constants.Exception_Head, ex);
        OperationResult result=new OperationResult();
        if (ex instanceof AbstractException) {
            ((AbstractException) ex).handler(result);
        } /*else if (ex instanceof IllegalArgumentException) {
            new IllegalParameterException(ex.getMessage()).handler(modelMap);
        } else if (ex instanceof UnauthorizedException) {
            modelMap.put("httpCode", HttpCode.FORBIDDEN.value());
            modelMap.put("msg", StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.FORBIDDEN.msg()));
        } */else {
            result.setHttpCode(HttpCode.INTERNAL_SERVER_ERROR.value());
            String msg = StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.INTERNAL_SERVER_ERROR.msg());
            result.setMessage(msg.length() > 100 ? "系统走神了,请稍候再试." : msg);
        }
        response.setContentType("application/json;charset=UTF-8");
        logger.info(JSON.toJSONString(result));
        byte[] bytes = JSON.toJSONBytes(result, SerializerFeature.DisableCircularReferenceDetect);
        response.getOutputStream().write(bytes);
    }
}
