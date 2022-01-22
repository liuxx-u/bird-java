package com.bird.service.common.incrementer;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuxx
 * @date 2019/8/23
 */
@Slf4j
public final class UUIDHexGenerator {

    private static final int RADIX = 36;
    private static final int TIME_BACK_OFFSET = 1000;
    private static final int MAX_PID = 1000000;
    private static final String DELIMITER = "-";
    private static final String MACHINE_ID = formatMachineId();
    private static final String JVM_PID = formatJvmPid();

    private static volatile Short SEQUENCE = 0;
    private static volatile long LAST_TIME_STAMP = timeGen();

    private UUIDHexGenerator() {
    }

    /**
     * 获取下一个 ID
     *
     * @return 下一个 ID
     */
    public static synchronized String generate() {
        long timestamp = timeGen();
        //闰秒
        if (timestamp < LAST_TIME_STAMP) {
            long offset = LAST_TIME_STAMP - timestamp;
            if (offset <= TIME_BACK_OFFSET) {
                try {
                    Thread.sleep(offset);
                    timestamp = timeGen();
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset), e);
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }

        if (LAST_TIME_STAMP == timestamp) {
            // 相同毫秒内，序列号自增
            if (++SEQUENCE <= 0) {
                // 同一毫秒的序列数已经达到最大
                timestamp = tilNextMillis();
            }
        } else {
            // 不同毫秒内，序列号置为 1
            SEQUENCE = 1;
        }

        LAST_TIME_STAMP = timestamp;

        // 时间戳部分 | 机器标识部分 | 进程标识部分 | 序列号部分
        return formatMillis(timestamp) + DELIMITER
                + MACHINE_ID + DELIMITER
                + JVM_PID + DELIMITER
                + formatSequence(SEQUENCE);
    }

    /**
     * 获取时间戳部分
     */
    private static String formatMillis(long timestamp) {
        String formatted = Long.toString(timestamp, RADIX);

        StringBuilder buf = new StringBuilder("000000000");
        buf.replace(9 - formatted.length(), 9, formatted);
        return buf.toString();
    }

    /**
     * 获取机器标识部分
     */
    private static String formatMachineId() {
        long machineId;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                machineId = toLong(ip.getAddress());
            } else {
                byte[] mac = network.getHardwareAddress();
                machineId = toLong(mac);
            }
        } catch (Exception e) {
            log.warn("获取机器id失败 " + e.getMessage());
            machineId = ThreadLocalRandom.current().nextInt();
        }

        String formatted = Long.toString(machineId, RADIX);

        StringBuilder buf = new StringBuilder("0000000000");
        buf.replace(10 - formatted.length(), 10, formatted);
        return buf.toString();
    }

    /**
     * 获取jvmPid
     */
    private static String formatJvmPid() {
        int pid = 0;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            // GET jvmPid
            try {
                pid = Integer.parseInt(name.split(StringPool.AT)[0]);
            } catch (Exception ex) {
                log.error("获取jvmPid失败", ex);
            }
        }

        String formatted = Integer.toString(pid % MAX_PID, RADIX);
        StringBuilder buf = new StringBuilder("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    /**
     * 获取序列号部分
     */
    private static String formatSequence(short sequence) {
        String formatted = Integer.toString(sequence, RADIX);
        StringBuilder buf = new StringBuilder("000");
        buf.replace(3 - formatted.length(), 3, formatted);
        return buf.toString();
    }

    /**
     * 将byte数组转换为整数，并取绝对值
     *
     * @param bytes byte数组
     * @return int.
     */
    private static long toLong(byte[] bytes) {
        long result = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            result += bytes[i] * Math.pow(0xFF, bytes.length - i - 1);
        }
        return Math.abs(result);
    }

    private static long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= LAST_TIME_STAMP) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }
}
