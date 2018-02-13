package com.bird.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bird.core.Constants;
import com.bird.core.HttpCode;
import com.bird.core.exception.AbstractException;
import com.bird.core.sso.ticket.TicketHandler;
import com.bird.core.sso.ticket.TicketInfo;
import com.bird.core.utils.CollectionWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by liuxx on 2017/5/25.
 */
public abstract class AbstractController implements Serializable{

    @Autowired
    protected TicketHandler ticketHandler;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取集合包装器方法
     * @param collection 需要操作的集合
     * @return 链式操作的集合包装器
     */
    protected <S> CollectionWrapper<S> cw(Collection<S> collection) {
        return CollectionWrapper.init(collection);
    }

    /** 获取当前用户Id */
    protected TicketInfo getUser(HttpServletRequest request) {
        return ticketHandler.getTicket(request);
    }

    protected Long getUserId(HttpServletRequest request) {
        TicketInfo ticketInfo = getUser(request);
        if (ticketInfo == null) return 0L;
        if (StringUtils.isBlank(ticketInfo.getUserId())) return 0L;
        return Long.valueOf(ticketInfo.getUserId());
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
            result.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
            String msg = StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.INTERNAL_SERVER_ERROR.msg());
            result.setMessage(msg.length() > 100 ? "系统走神了,请稍候再试." : msg);
        }
        response.setContentType("application/json;charset=UTF-8");
        logger.info(JSON.toJSONString(result));
        byte[] bytes = JSON.toJSONBytes(result, SerializerFeature.DisableCircularReferenceDetect);
        response.getOutputStream().write(bytes);
    }
}
