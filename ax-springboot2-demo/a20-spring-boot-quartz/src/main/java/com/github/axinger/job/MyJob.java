package com.github.axinger.job;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MyJob.java
 * @description TODO
 * @createTime 2022年03月20日 01:42:00
 */
@Slf4j
public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        // 从定时任务中,取参数
        final JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        final String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("定时任务 key = {}", name + ",dataMap = " + JSONObject.toJSONString(dataMap));

    }
}
