package com.github.axinger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@Slf4j
public class TestJob {

    //cron表达式通常由6或7个字段组成（Quartz等框架中可能包含秒字段，因此为7个字段），每个字段使用空格分隔，分别表示：
    // 每5秒
    @Scheduled(cron = "0/5 * * * * *")
    public void test1() {
        log.info("test1={}", LocalDateTime.now());

    }

    @Scheduled(cron = "0 0 7-8 * * 5")
    public void test121() {
        log.info("test1={}", LocalDateTime.now());
    }

    // ?：只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。
    // 因为DayofMonth和DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法
    @Scheduled(cron = "13 13 15 20 * ?")
    public void test11() {
        log.info("test11={}", LocalDateTime.now());
    }

    // -：表示范围。例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次
    @Scheduled(cron = "5-20 * * * * *")
    public void test12() {
        log.info("test12={}", LocalDateTime.now());
    }

    // ,：表示列出枚举值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
    @Scheduled(cron = "5,20 * * * * *")
    public void test13() {
        log.info("test13={}", LocalDateTime.now());
    }


    //L：表示最后，只能出现在DayOfWeek和DayOfMonth域。如果在DayOfWeek域使用5L,意味着在最后的一个星期四触发。
    @Scheduled(cron = "* * * * * 5L")
    public void test14() {
        log.info("test13={}", LocalDateTime.now());
    }

    @Scheduled(cron = "* * * L * *")
    public void test141() {
        log.info("test13={}", LocalDateTime.now());
    }

    // W:表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。
    // 例如：在 DayOfMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。
    // 如果5日是星期天，则在6日(周一)触发；如果5日在星期一到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份 。
    @Scheduled(cron = "* * * 5W * *")
    // weekend
    public void test15() {
        log.info("test13={}", LocalDateTime.now());
    }

    //LW:这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五
    // last weekend
    @Scheduled(cron = "* * * LW *  *")
    public void test16() {
        log.info("test13={}", LocalDateTime.now());
    }

    //#:用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。
    @Scheduled(cron = "* * * *  * 4#2")
    public void test17() {
        log.info("test13={}", LocalDateTime.now());
    }

    @Scheduled(cron = "0 * * * * ?")
    public void test3() {
        log.info("test3={}", LocalDateTime.now());
    }

    // 22:30 - 22:59 每分钟执行
    @Scheduled(cron = "0 30-59 22 * * *")
    public void test41() {
        log.info("test3={}", LocalDateTime.now());
    }

    // 23:00 - 23:30 每分钟执行
    @Scheduled(cron = "0 0-30 23 * * *")
    public void test42() {
        System.out.println("Task running in 23:00 - 23:30");
    }
}
