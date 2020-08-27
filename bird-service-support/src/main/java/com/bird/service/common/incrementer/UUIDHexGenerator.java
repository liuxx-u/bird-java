package com.bird.service.common.incrementer;

import java.net.InetAddress;
import java.util.Random;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public final class UUIDHexGenerator {

    private final static int RADIX = 36;
    private final static String DELIMITER = "-";
    private final static String FORMAT_IP;
    private final static Random RANDOM = new Random();

    static {
        int ipAddress;
        try {
            ipAddress = Math.abs(toInt(InetAddress.getLocalHost().getAddress()));
        } catch (Exception e) {
            ipAddress = 0;
        }
        FORMAT_IP = format(ipAddress);
    }

    private UUIDHexGenerator() {
    }

    private static int counter = 0;

    public static String generate() {
        return format(System.currentTimeMillis()) + DELIMITER
                + FORMAT_IP + DELIMITER
                + format(Math.abs(RANDOM.nextInt())) + DELIMITER
                + format(getCount());
    }

    private static String format(long longValue) {
        String formatted = Long.toString(longValue, RADIX);

        StringBuilder buf = new StringBuilder("000000000");
        buf.replace(9 - formatted.length(), 9, formatted);
        return buf.toString();
    }

    private static String format(int intValue) {
        String formatted = Integer.toString(intValue, RADIX);
        StringBuilder buf = new StringBuilder("000000");
        buf.replace(6 - formatted.length(), 6, formatted);
        return buf.toString();
    }

    /**
     * 计数器
     */
    private static int getCount() {
        synchronized (UUIDHexGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }

    /**
     * 将byte数组转换为整数
     *
     * @param bytes byte数组
     * @return int.
     */
    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            result += bytes[i] * Math.pow(0xFF, bytes.length - i - 1);
        }
        return result;
    }
}
