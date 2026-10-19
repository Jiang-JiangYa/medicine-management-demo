package org.example.myspringboot.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能：生成单号，如：C20240929001。 C + 8位日期 + 3位递增数字
 * 作者：yangjia
 * 日期：2025/3/26 上午11:34
 */
public class generateBillno {
    // 使用静态变量保存当前日期和递增序号
    private static String currentDate;
    private static AtomicInteger sequence = new AtomicInteger(1);

    /** 生成入库单号 */
    public static synchronized String generateInStoreCode() {
        // 1. 获取当前日期（格式：yyyyMMdd）
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 如果日期变化，重置序号为1
        if (!today.equals(currentDate)) {
            currentDate = today;
            sequence.set(1); // 每日从1开始
        }

        // 3. 生成序号（格式化为3位，如001）
        int seq = sequence.getAndIncrement();
        if (seq > 999) {
            throw new IllegalStateException("当日序号已超过999，无法生成更多单号！");
        }
        String sequencePart = String.format("%03d", seq);

        // 4. 拼接单号
        return "C" + currentDate + sequencePart;
    }

    /** 生成退库单号 */
    public static synchronized String generateRefundingCode() {
        // 1. 获取当前日期（格式：yyyyMMdd）
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 如果日期变化，重置序号为1
        if (!today.equals(currentDate)) {
            currentDate = today;
            sequence.set(1); // 每日从1开始
        }

        // 3. 生成序号（格式化为3位，如001）
        int seq = sequence.getAndIncrement();
        if (seq > 999) {
            throw new IllegalStateException("当日序号已超过999，无法生成更多单号！");
        }
        String sequencePart = String.format("%03d", seq);

        // 4. 拼接单号
        return "D" + currentDate + sequencePart;
    }

    /** 生成出库单号 */
    public static synchronized String generateOutStoreCode() {
        // 1. 获取当前日期（格式：yyyyMMdd）
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 如果日期变化，重置序号为1
        if (!today.equals(currentDate)) {
            currentDate = today;
            sequence.set(1); // 每日从1开始
        }

        // 3. 生成序号（格式化为3位，如001）
        int seq = sequence.getAndIncrement();
        if (seq > 999) {
            throw new IllegalStateException("当日序号已超过999，无法生成更多单号！");
        }
        String sequencePart = String.format("%03d", seq);

        // 4. 拼接单号
        return "E" + currentDate + sequencePart;
    }
}
