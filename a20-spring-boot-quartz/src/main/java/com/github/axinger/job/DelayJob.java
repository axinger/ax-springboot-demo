package com.github.axinger.job;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;


@Slf4j
public class DelayJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        // 从定时任务中,取参数
        final JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        final String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("延迟任务 key = {}", name + ",dataMap = " + JSONObject.toJSONString(dataMap));

    }
}
