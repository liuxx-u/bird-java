package com.bird.service.common.incrementer;

import com.baomidou.mybatisplus.incrementer.IKeyGenerator;

import java.net.InetAddress;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public class UUIDHexGenerator implements IKeyGenerator {

    private final static String DELIMITER = "-";

    private final static String FORMAT_IP;

    private final static String FORMAT_JVM;

    static {
        int ipAddress;
        try {
            ipAddress = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipAddress = 0;
        }
        FORMAT_IP = format(ipAddress);
        FORMAT_JVM = format((int) (System.currentTimeMillis() >>> 8));
    }

    private static short counter = (short) 0;


    public static String generate() {
        return format(getLoTime()) + DELIMITER
                + format(getHiTime()) + DELIMITER
                + FORMAT_JVM + DELIMITER
                + FORMAT_IP + DELIMITER
                + format(getCount());
    }

    private static String format(int intValue) {
        String formatted = Integer.toHexString(intValue);
        StringBuilder buf = new StringBuilder("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    private static String format(short shortValue) {
        String formatted = Integer.toHexString(shortValue);
        StringBuilder buf = new StringBuilder("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there
     * are > Short.MAX_VALUE instances created in a millisecond)
     */
    private static short getCount() {
        synchronized (UUIDHexGenerator.class) {
            if (counter < 0) counter = 0;
            return counter++;
        }
    }

    /**
     * Unique down to millisecond
     */
    private static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    private static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    /**
     * Custom algorithm used to generate an int from a series of bytes.
     * <p/>
     * NOTE : this is different than interpreting the incoming bytes as an int value!
     *
     * @param bytes The bytes to use in generating the int.
     * @return The generated int.
     */
    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }


    /**
     * <p>
     * 执行 key 生成 SQL
     * </p>
     *
     * @param incrementerName 序列名称
     * @return key
     */
    @Override
    public String executeSql(String incrementerName) {
        String uid = UUIDHexGenerator.generate();
        return "select '" + uid + "' from dual";
    }
}
