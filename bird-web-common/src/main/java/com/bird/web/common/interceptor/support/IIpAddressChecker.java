package com.bird.web.common.interceptor.support;

/**
 * @author liuxx
 * @date 2018/4/18
 *
 * IP地址检查器
 */
public interface IIpAddressChecker {

    /**
     * 检查
     * @param ip ip地址
     * @return
     */
    boolean check(String ip);
}
