package com.github.axinger;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName DateTest.java
 * @description TODO
 * @createTime 2022年07月28日 13:56:00
 */

public class LocalDateTimeTest {


    @Test
    void test_before() {

        LocalDateTime now = LocalDateTime.now();
        final LocalDateTime beginOfDay = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());

        final LocalDateTime endOfDay = LocalDateTimeUtil.endOfDay(LocalDateTime.now());


        System.out.println("localDateTime.isBefore(LocalDateTime.now()) = " + beginOfDay.isBefore(LocalDateTime.now()));

        System.out.println(LocalDateTime.now().isBefore(endOfDay));
        System.out.println(now.isBefore(now));
        System.out.println(now.isAfter(now));
        System.out.println(now.equals(now));

        System.out.println(now.compareTo(now));
        System.out.println(beginOfDay.compareTo(endOfDay));
        System.out.println(endOfDay.compareTo(beginOfDay));

        System.out.println(now.compareTo(now) < 1);
        System.out.println(beginOfDay.compareTo(endOfDay) < 1);
        System.out.println(endOfDay.compareTo(beginOfDay) < 1);
    }

    @Test
    void test_withDayOfYear() {

        // System.out.println("月初 = " + LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withDayOfMonth(1)));
        // System.out.println("月末 = " + LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withDayOfMonth(31)));


        LocalDateTime _2th = LocalDateTimeUtil.parse("2022-02-28 09:00:00", "yyyy-MM-dd HH:mm:ss").with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("_2th = " + _2th);
        System.out.println("2月末 = " + LocalDateTimeUtil.endOfDay(_2th));

        System.out.println("年初 = " + LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withDayOfYear(1)));

        System.out.println(LocalDateTime.now().withDayOfYear(1));


    }

    @Test
    void test_last_month() {
        // 上个月最后一天结束时间
        final LocalDateTime localDateTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withDayOfMonth(1));

        LocalDateTime of = LocalDateTimeUtil.offset(localDateTime, -1, ChronoUnit.MICROS);
        System.out.println("of = " + of);

    }


    @Test
    void test_1() {
        final LocalDateTime localDateTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withDayOfMonth(1));
        System.out.println("localDateTime = " + localDateTime);

    }

    @Test
    void test_2() {
        final LocalDateTime localDateTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now().withMonth(1).withDayOfMonth(1));
        System.out.println("localDateTime = " + localDateTime);

    }

    @Test
    void test_3() {
        // 将 LocalDateTime 对象转化为 Date 对象
        Date dateTime = new DateTime(LocalDateTime.now());
        System.out.println("dateTime = " + dateTime);

    }
}
