package com.bird.web.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author liuxx
 * @since 2020/9/4
 */
public final class IpHelper {

    private final static int IP_MAX_LENGTH = 32;
    private final static int SEGMENT_COUNT = 4;
    private final static int MAX_SEGMENT_VALUE = 255;
    private final static String IP_DELIMITER_REGEX = "\\.";
    private final static String SEGMENT_DELIMITER_REGEX = "/";
    private final static String DEFAULT_SELF_SEGMENT = "0";

    /**
     * 校验ip是否在指定的ip段之内
     *
     * @param range ip段，x.x.x.x/x
     * @param ip    指定ip
     * @return 是否在ip范围内
     */
    public static boolean checkIpRange(String range, String ip) {
        if (!checkIp4Address(ip)) {
            return false;
        }

        String startIp = getStartIp(range);
        String endIp = getEndIp(range);
        if (Objects.equals(startIp, ip) || Objects.equals(endIp, ip)) {
            return true;
        } else if (Objects.equals(startIp, endIp)) {
            return false;
        }

        Integer[] startIpArray = getIpArray(startIp);
        Integer[] endIpArray = getIpArray(endIp);
        if (startIpArray.length != SEGMENT_COUNT || endIpArray.length != SEGMENT_COUNT) {
            return false;
        }
        Integer[] ipArray = getIpArray(ip);
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            if (ipArray[i] > startIpArray[i] && ipArray[i] < endIpArray[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据掩码位数计算掩码
     *
     * @param maskIndex 掩码位
     * @return 子网掩码
     */
    private static String getNetMask(String maskIndex) {
        StringBuilder mask = new StringBuilder();
        int netMask;
        try {
            netMask = Integer.parseInt(maskIndex);
        } catch (NumberFormatException e) {
            return null;
        }
        if (netMask > IP_MAX_LENGTH) {
            return null;
        }
        // 子网掩码为1占了几个字节
        int num1 = netMask / 8;
        // 子网掩码的补位位数
        int num2 = netMask % 8;
        int[] array = new int[SEGMENT_COUNT];
        for (int i = 0; i < num1; i++) {
            array[i] = 255;
        }
        for (int i = num1; i < SEGMENT_COUNT; i++) {
            array[i] = 0;
        }
        for (int i = 0; i < num2; num2--) {
            array[num1] += 1 << 8 - num2;
        }
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            if (i == 3) {
                mask.append(array[i]);
            } else {
                mask.append(array[i]).append(".");
            }
        }
        return mask.toString();
    }

    /**
     * 根据网段计算起始IP 网段格式:x.x.x.x/x
     * 一个网段0一般为网络地址,255一般为广播地址.
     * 起始IP计算:网段与掩码相与之后加一的IP地址
     *
     * @param segment 网段
     * @return 起始IP
     */
    private static String getStartIp(String segment) {
        if (StringUtils.isBlank(segment)) {
            return null;
        }
        String[] arr = segment.split(SEGMENT_DELIMITER_REGEX);
        String ip = arr[0];
        if (!checkIp4Address(ip)) {
            return null;
        }
        if (arr.length == 1) {
            return ip;
        }

        String maskIndex = arr[1];
        if (Objects.equals(maskIndex, DEFAULT_SELF_SEGMENT)) {
            return ip;
        }
        String mask = IpHelper.getNetMask(maskIndex);
        if (mask == null || !checkIp4Address(mask)) {
            return ip;
        }
        Integer[] ipArray = getIpArray(ip);
        Integer[] netMaskArray = getIpArray(mask);
        StringBuilder startIp = new StringBuilder();
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            ipArray[i] = ipArray[i] & netMaskArray[i];
            if (i == SEGMENT_COUNT - 1) {
                startIp.append(ipArray[i] + 1);
            } else {
                startIp.append(ipArray[i]).append(".");
            }
        }
        return startIp.toString();
    }

    /**
     * 根据网段计算结束IP
     *
     * @param segment 网段
     * @return 结束IP
     */
    private static String getEndIp(String segment) {
        String startIp = getStartIp(segment);
        if (startIp == null || !checkIp4Address(startIp)) {
            return null;
        }
        String[] arr = segment.split(SEGMENT_DELIMITER_REGEX);
        if (arr.length == 1) {
            return startIp;
        }
        int maskIndex;
        Integer[] ipArray;
        try {
            maskIndex = Integer.parseInt(arr[1]);
            ipArray = getIpArray(startIp);
        } catch (NumberFormatException e) {
            return startIp;
        }

        //实际需要的IP个数
        int hostNumber;
        int[] startIpArray = new int[SEGMENT_COUNT];
        hostNumber = 1 << IP_MAX_LENGTH - (maskIndex);
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            startIpArray[i] = ipArray[i];
            if (i == 3) {
                startIpArray[i] = startIpArray[i] - 1;
                break;
            }
        }
        startIpArray[3] = startIpArray[3] + (hostNumber - 1);

        if (startIpArray[3] > MAX_SEGMENT_VALUE) {
            int k = startIpArray[3] / 256;
            startIpArray[3] = startIpArray[3] % 256;
            startIpArray[2] = startIpArray[2] + k;
        }
        if (startIpArray[2] > MAX_SEGMENT_VALUE) {
            int j = startIpArray[2] / 256;
            startIpArray[2] = startIpArray[2] % 256;
            startIpArray[1] = startIpArray[1] + j;
            if (startIpArray[1] > MAX_SEGMENT_VALUE) {
                int k = startIpArray[1] / 256;
                startIpArray[1] = startIpArray[1] % 256;
                startIpArray[0] = startIpArray[0] + k;
            }
        }
        StringBuilder endIp = new StringBuilder();
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            if (i == SEGMENT_COUNT - 1) {
                endIp.append(startIpArray[i] - 1);
            } else {
                endIp.append(ipArray[i]).append(".");
            }
        }
        return endIp.toString();
    }

    /**
     * 校验IP格式是否正确
     *
     * @param ipAddress ipv4地址
     * @return 是否合法的ip
     */
    private static Boolean checkIp4Address(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        String regex = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * ip地址解析为十进制数组
     *
     * @param ipAddress ipv4地址
     * @return ip数组
     */
    private static Integer[] getIpArray(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return new Integer[0];
        }
        return Arrays.stream(ipAddress.split(IP_DELIMITER_REGEX)).map(Integer::parseInt).toArray(Integer[]::new);
    }
}
