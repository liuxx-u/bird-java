package com.bird.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/5/17
 */
@Slf4j
public final class SystemHelper {

    /**
     * 获取机器ip
     * @return 机器ip集合
     */
    public static List<String> getLocalIps() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        ips.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("robbin负载均衡获取本机ip失败", ex);
        }
        return ips;
    }
}
