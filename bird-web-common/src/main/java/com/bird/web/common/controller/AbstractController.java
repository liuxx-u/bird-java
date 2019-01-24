package com.bird.web.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bird.core.HttpCode;
import com.bird.core.OperationResult;
import com.bird.core.exception.AbstractException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author liuxx
 * Created by liuxx on 2017/5/25.
 */
public abstract class AbstractController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 当前的请求
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 当前的响应
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * 异常处理
     */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        OperationResult result = new OperationResult();

        if (ex instanceof AbstractException) {
            logger.warn(ex.getMessage(), ex);
            result = OperationResult.Fail((AbstractException) ex);
        } else {
            logger.error(ex.getMessage(), ex);
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
