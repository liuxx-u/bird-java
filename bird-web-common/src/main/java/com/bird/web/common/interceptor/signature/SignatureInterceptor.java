package com.bird.web.common.interceptor.signature;

import com.bird.web.common.utils.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuxx
 * @date 2019/7/23
 */
public class SignatureInterceptor extends HandlerInterceptorAdapter {

    private final static String ERROR_MESSAGE = "签名验证失败";

    /**
     * 合法的请求时间间隔，默认5分钟
     */
    @Value("${bird.web.signature.timeSpan:5}")
    private int timeSpan;

    @Autowired(required = false)
    private ISignatureVerifier signatureVerifier;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        if (RequestHelper.isMultipartContent(request)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        SignatureCheck signatureCheck = handlerMethod.getMethodAnnotation(SignatureCheck.class);
        if (signatureCheck != null && signatureCheck.ignore()) return true;

        SignatureInfo signatureInfo = SignatureInfo.init(request);
        if (!signatureInfo.checkValue() || !signatureInfo.checkTimestamp(timeSpan)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, ERROR_MESSAGE);
            return false;
        }

        // 默认使用容器中的ISignatureVerifier进行验证，没有使用默认的验证方式
        if (signatureVerifier != null) {
            if (!signatureVerifier.checkSignature(signatureInfo)) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, ERROR_MESSAGE);
                return false;
            }
        } else {
            if (!signatureInfo.checkSignature()) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
