package com.github.axinger.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.axing.common.quartz.model.CronTaskPOJO;
import com.axing.common.quartz.service.QuartzTemplate;
import com.axing.common.response.dto.Result;
import com.github.axinger.job.DelayJob;
import com.github.axinger.job.MyJob;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@RestController
@Slf4j
@RequestMapping("/job")
@Tag(name = "任务")
public class JobController {

    private final CompletableFuture<Boolean> future = new CompletableFuture<>();
    @Autowired
    private QuartzTemplate quartzTemplate;
    @Resource
    private Scheduler scheduler;

    @Operation(summary = "添加一个任务", description = "添加描述")
    @GetMapping("/add")
    public Object addJob(String id) {
        log.info("add id = {}", id);
        CronTaskPOJO cronTaskPOJO = new CronTaskPOJO();
        cronTaskPOJO.setJobName(id);
        cronTaskPOJO.setCronExpression("0/" + 6 + " * * * * ?");
        cronTaskPOJO.setJobClass(MyJob.class);
        final Map<String, Object> map = new HashMap<>(16);
        map.put("name", "jim");
        map.put("age", 18);
        cronTaskPOJO.setParameterMap(map);
        quartzTemplate.addJob(cronTaskPOJO);
        return Result.success();
    }

    @Operation(summary = "是否存在一个任务", description = "添加描述")
    @GetMapping("/notExists")
    public Object notExists(String id) {
        Boolean b = quartzTemplate.isExists(id, null);
        return Result.success(b ? "存在" : "不存在");
    }

    @GetMapping("/delete")
    public Object delete(String id) {
        log.info("delete id = {}", id);
        final CronTaskPOJO cronTaskPOJO = new CronTaskPOJO();
        cronTaskPOJO.setJobName(id);
        quartzTemplate.deleteJob(cronTaskPOJO);
        return Result.success();
    }

    /**
     * 恢复
     */
    @GetMapping("/resume")
    public Object resume(String id) {
        log.info("resume id = {}", id);
        final CronTaskPOJO cronTaskPOJO = new CronTaskPOJO();
        cronTaskPOJO.setJobName(id);
        quartzTemplate.resumeJob(cronTaskPOJO);
        return Result.success();
    }

    /**
     * 暂停
     */
    @GetMapping("/pauseJob")
    public Object pauseJob(String id) {
        log.info("pauseJob id = {}", id);
        final CronTaskPOJO cronTaskPOJO = new CronTaskPOJO();
        cronTaskPOJO.setJobName(id);
        quartzTemplate.resumeJob(cronTaskPOJO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<?> all() {
        List<Object> list = new ArrayList<>();
        list.add(quartzTemplate.getAllJob());
        return Result.success(list);
    }

    @SneakyThrows
    @GetMapping("/addJob2")
    public Result<?> addJob2(String id) {
        log.info("id = {}", id);
        CronTaskPOJO task = new CronTaskPOJO();
        task.setJobName(id);
        task.setCronExpression("0/" + 5 + " * * * * ?");
        task.setJobClass(MyJob.class);

        final Map<String, Object> map = new HashMap<>(16);
        map.put("name", "jim");
        map.put("age", 18);
        task.setParameterMap(map);


        // 任务名称和组构成任务key
        JobDetail jobDetail =
                JobBuilder.newJob(task.getJobClass())
                        .withIdentity(task.getJobName(), task.getGroupName())
                        // 参数
                        .usingJobData("parameter", task.getParameter())
                        .build();

        // 定义调度触发规则、使用cornTrigger规则、触发器key
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(task.getJobName(), task.getGroupName())
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .startNow()
                .build();
        // 把作业和触发器注册到任务调度中

        scheduler.scheduleJob(jobDetail, trigger);
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }

        return Result.success();
    }

    @Operation(summary = "延迟任务", description = "添加描述")
    @SneakyThrows
    @GetMapping("/delay")
    public Result<?> delay() {

        // 3.创建JobDetail，
        JobDetail jb = JobBuilder.newJob(DelayJob.class)
                .withIdentity("delay-job")
                .usingJobData("delayTask", "这是一个延迟任务")
                .build();

        // 4.创建Trigger
        DateTime offset = DateUtil.offsetSecond(new Date(), 5);
        Trigger t = TriggerBuilder.newTrigger()
                //任务的触发时间就是延迟任务到的延迟时间
                .startAt(offset)
                .build();

        // 5.注册任务和定时器
        Date scheduled = scheduler.scheduleJob(jb, t);
        log.info("任务提交成功，任务执行时间={},将在={}执行", scheduled, offset);

        return Result.success();
    }
}
