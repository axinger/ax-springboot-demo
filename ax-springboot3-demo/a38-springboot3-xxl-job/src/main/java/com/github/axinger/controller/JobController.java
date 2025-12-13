package com.github.axinger.controller;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JobController {

    @Autowired
    private XxlJobSpringExecutor xxlJobSpringExecutor;

    @GetMapping("/start")
    public void start() throws Exception {
        String cron = "";
        Integer id = 1;
        String taskName = "";


        //1)将数据封装成Map   Map->XxlJobInfo
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("jobGroup", 1);
        dataMap.put("jobDesc", "动态定时任务作业");
        dataMap.put("author", "张三");
        dataMap.put("scheduleType", "CRON");
        //执行时间
        dataMap.put("scheduleConf", cron);  //动态的
        dataMap.put("cronGen_display", cron);  //动态的
        dataMap.put("glueType", "BEAN");
        //哪个任务
        dataMap.put("executorHandler", taskName); //动态的
        dataMap.put("executorParam", id);  //订单ID
        dataMap.put("executorRouteStrategy", "FIRST");
        dataMap.put("misfireStrategy", "DO_NOTHING");
        dataMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
        dataMap.put("executorTimeout", 0);
        dataMap.put("executorFailRetryCount", 0);
        dataMap.put("glueRemark", "GLUE代码初始化");


        //2）XxlJobRemotingUtil实现远程调用
        // url  、参数 、accessToken
        String url = "http://localhost:8080/xxl-job-admin/jobinfo/json/add";
        String token = "7b226964223a312c22757365726e616d65223a2261646d696e222c2270617373776f7264223a226531306164633339343962613539616262653536653035376632306638383365222c22726f6c65223a312c227065726d697373696f6e223a6e756c6c7d";
        ReturnT returnT = XxlJobRemotingUtil.postBody(
                url,    //请求地址
                token,  //令牌信息
                10,
                dataMap,
                ReturnT.class);//返回数据转换对象
        System.out.println(returnT);


    }

    @GetMapping("/stop")
    public void stop() {

        // 获取JobHandler
//        IJobHandler jobHandler = xxlJobExecutor.loadJobHandler("demoJobHandler");
        IJobHandler jobHandler = XxlJobExecutor.loadJobHandler("demoJobHandler");


        if (jobHandler != null) {
            try {
                // 执行任务
                jobHandler.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("myJobHandler 未找到");
        }

    }
}
