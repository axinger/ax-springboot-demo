package com.axing.common.quartz.service;

import com.axing.common.quartz.model.CronTaskPOJO;

import java.util.List;

/**
 * @author xing
 * @date 2022/3/18 11:25
 */
public interface JobService {
//
//    /**
//     * 开始执行所有任务
//     */
//    void startJob();
//
//    /**
//     * 获取Job信息
//     *
//     * @param name
//     * @param group
//     * @return
//     */
//    String getJobInfo(String name, String group);
//
//
//    /**
//     * 修改某个任务的执行时间
//     *
//     * @param name
//     * @param group
//     * @param time
//     * @return
//     */
//    boolean modifyJob(String name, String group, String time);
//
//    /**
//     * 暂停所有任务
//     *
//     * @throws SchedulerException
//     */
//    void pauseAllJob() throws SchedulerException;
//
//    /**
//     * 暂停某个任务
//     *
//     * @param name
//     * @param group
//     */
//    @SneakyThrows
//    void pauseJob(String name, String group);
//
//    /**
//     * 恢复所有任务
//     *
//     * @throws SchedulerException
//     */
//    void resumeAllJob() throws SchedulerException;
//
//    /**
//     * 恢复某个任务
//     *
//     * @param name
//     * @param group
//     */
//    @SneakyThrows
//    void resumeJob(String name, String group);
//
//    /**
//     * 删除某个任务
//     *
//     * @param name
//     * @param group
//     */
//    void deleteJob(String name, String group);
//
//    /**
//     * 修改一个任务的触发时间
//     *
//     * @param triggerName
//     * @param triggerGroupName
//     * @param cron
//     */
//    void modifyJobTime(String triggerName, String triggerGroupName, String cron);
//
//    /**
//     * 移除一个任务
//     *
//     * @param jobName
//     * @param jobGroupName
//     * @param triggerName
//     * @param triggerGroupName
//     */
//    void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName);
//

    /**
     * 获取任务是否存在
     * TATE_BLOCKED 4 阻塞 STATE_COMPLETE 2 完成 STATE_ERROR 3 错误 STATE_NONE -1 不存在 STATE_NORMAL 0 正常 STATE_PAUSED 1 暂停
     *
     * @param name
     * @param group
     * @return
     */
    Boolean isExists(String name, String group);

    /**
     * 添加一个定时任务
     *
     * @param task
     */
    void addJob(CronTaskPOJO task);


    /**
     * 获取所有任务
     *
     * @return
     */
    List<CronTaskPOJO> getAllJob();


    /**
     * 所有正在运行的job
     *
     * @return
     */
    List<CronTaskPOJO> getRunningJob();


    /**
     * 暂停一个job
     *
     * @param task
     */
    void pauseJob(CronTaskPOJO task);


    /**
     * 恢复一个job
     *
     * @param task
     */
    void resumeJob(CronTaskPOJO task);


    /**
     * 删除一个job
     *
     * @param task
     * @return
     */
    boolean deleteJob(CronTaskPOJO task);

    /**
     * 立即执行job
     *
     * @param task
     */
    void runJobNow(CronTaskPOJO task);

    /**
     * 更新job时间表达式
     *
     * @param task
     */
    void updateJobCron(CronTaskPOJO task);
}
