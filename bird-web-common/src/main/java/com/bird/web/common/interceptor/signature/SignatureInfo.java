package com.bird.web.common.interceptor.signature;

import com.bird.web.common.utils.RequestHelper;
import lombok.Data;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author liuxx
 * @date 2019/7/23
 */
@Data
public class SignatureInfo implements Serializable {

    private final static String NONCE_HEADER_NAME = "nonce";
    private final static String TIMESTAMP_HEADER_NAME = "timestamp";
    private final static String SIGN_HEADER_NAME = "sign";

    /**
     * 随机数
     */
    private String nonce;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 签名
     */
    private String sign;


    /**
     * 初始化签名参数
     * @param request 请求
     * @return 签名信息
     */
    static SignatureInfo init(HttpServletRequest request){
        SignatureInfo signatureInfo = new SignatureInfo();
        signatureInfo.setNonce(request.getHeader(NONCE_HEADER_NAME));
        signatureInfo.setTimestamp(request.getHeader(TIMESTAMP_HEADER_NAME));
        signatureInfo.setSign(request.getHeader(SIGN_HEADER_NAME));

        HttpMethod method = HttpMethod.resolve(request.getMethod());
        if(BodyReaderFilter.SUPPORT_HTTP_METHODS.contains(method)){
            signatureInfo.setParams(RequestHelper.getBodyString(request));
        }else {
            signatureInfo.setParams(RequestHelper.getBodyString(request));
        }
        return signatureInfo;
    }

    /**
     * 检查参数是否为空
     * @return true or false
     */
    Boolean checkValue(){
        return StringUtils.isNotBlank(this.nonce) && StringUtils.isNotBlank(this.timestamp) && StringUtils.isNotBlank(this.sign);
    }

    /**
     * 检查时间戳是否合法
     * @return true or false
     */
    Boolean checkTimestamp(Integer timeSpan) {
        try {
            long time = Long.parseLong(this.timestamp);
            return Math.abs(System.currentTimeMillis() - time) <= timeSpan * 60 * 1000;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * 默认的签名验证方法
     *
     * sign = md5(nonce + timestamp + base64(param))
     *
     * @return true or false
     */
    Boolean checkSignature(){
        String origin = this.nonce + this.timestamp;
        if(StringUtils.isNotBlank(this.params)){
            origin = origin + Base64.getEncoder().encodeToString(this.params.getBytes(Charset.forName("utf-8")));
        }
        String signature = DigestUtils.md5DigestAsHex(origin.getBytes(Charset.forName("utf-8")));
        return signature.equals(this.sign);
    }

    public static void main(String[] args) {
        String nonce = "123456";
        String timestamp = String.valueOf(System.currentTimeMillis());
        System.out.println(timestamp);
        String param = "{\"label\":\"liuxx\",\"value\":\"1\"}";
        String origin = nonce + timestamp + Base64.getEncoder().encodeToString(param.getBytes(Charset.forName("utf-8")));
        String signature = DigestUtils.md5DigestAsHex(origin.getBytes(Charset.forName("utf-8")));
        System.out.println(signature);
    }
}
