package com.bird.web.api.configurer;

import com.bird.web.common.interceptor.support.IIpAddressChecker;
import org.springframework.stereotype.Component;

/**
 * @author liuxx
 * @date 2018/4/19
 */
@Component
public class SimpleIpAddressChecker implements IIpAddressChecker {

    /**
     * 检查
     *
     * @param ip ip地址
     * @return
     */
    @Override
    public boolean check(String ip) {
        return true;
    }
}
