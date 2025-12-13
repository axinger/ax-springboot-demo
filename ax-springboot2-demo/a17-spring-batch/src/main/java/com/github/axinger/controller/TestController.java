package com.github.axinger.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    @Resource
    private JobLauncher jobLauncher;

    @Resource(name = "userJob")
    private Job job1;


    @GetMapping("/job")
    public Object runImportUserJob() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = new Date().getTime();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", time)
                .addString("selectDate", LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd"))
                .toJobParameters();

        log.info("添加启动参数={}", time);

        JobExecution run = jobLauncher.run(job1, jobParameters);
        ExitStatus exitStatus = run.getExitStatus();
        stopWatch.stop();

        Map<String, Object> map = new HashMap<>();
        map.put("耗时", stopWatch.getTotalTimeSeconds());
        map.put("status", exitStatus.getExitCode());
        map.put("status2", run.getStatus());
        log.info("任务结束={}", map);
        return map;
    }
}
