package com.bird.web.common.security.signature;

/**
 * @author liuxx
 * @date 2019/7/24
 */
public interface ISignatureVerifier {

    /**
     * 校验 签名是否合法
     * @param signature 签名信息
     * @return true or false
     */
    Boolean checkSignature(SignatureInfo signature);
}
