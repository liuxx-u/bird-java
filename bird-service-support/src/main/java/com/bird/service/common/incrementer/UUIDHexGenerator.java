package com.bird.service.common.incrementer;

import java.net.InetAddress;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public final class UUIDHexGenerator {

    private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

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

    private UUIDHexGenerator(){}

    private static short counter = (short) 0;


    public static String generate() {
        return format(System.currentTimeMillis()) + DELIMITER
                + format(getHiTime()) + DELIMITER
                + FORMAT_JVM + DELIMITER
                + FORMAT_IP + DELIMITER
                + format(getCount());
    }

    private static String format(long longValue){
        int digitIndex;
        long longPositive = Math.abs(longValue);
        //36进制
        int radix = 36;
        char[] outDigits = new char[37];
        for (digitIndex = 0; digitIndex <= 36; digitIndex++){
            if (longPositive == 0) { break; }
            outDigits[outDigits.length - digitIndex - 1] = digits[(int) (longPositive %  radix)];
            longPositive /= radix;
        }
        String formatted = new String(outDigits, outDigits.length - digitIndex, digitIndex);

        StringBuilder buf = new StringBuilder("000000000");
        buf.replace(9 - formatted.length(), 9, formatted);
        return buf.toString();
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
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }

    /**
     * Unique down to millisecond
     */
    private static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
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
}
