package com.drizzle.job.scheduler.springbatch.scheduler;

import com.drizzle.job.scheduler.springbatch.config.properties.JobSchedulerProperties;
import com.drizzle.job.scheduler.springbatch.scheduler.helper.CrontabHelper;
import com.drizzle.job.scheduler.springbatch.scheduler.model.JobConfig;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 调度器
 * @Author Administrator
 * @Date 2020/11/8 19:04
 */
@Slf4j
@Component
public class SchedulerService {

    @Autowired
    private JobSchedulerProperties jobSchedulerProperties;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobExplorer jobExplorer;

    // job名称与下次执行时间映射关系，用来判断jobs是否应该开始执行
    private static Map<String, DateTime> jobExecutionMap = new ConcurrentHashMap<>();

    /**
     * 按照配置的作业调度频率（cron表达式），调度job，默认为`每分钟一次`
     */
    @Scheduled(cron = "${job.scheduler.frequency:*/5 * * * * ?}")
    private void jobScheduler() {

        log.info("scheduler execute :[{}]", DateTime.now());

        List<JobConfig> jobConfigList = jobSchedulerProperties.getJobConfigList();

        for (JobConfig jobConfig : jobConfigList) {

            Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(jobConfig.getJobName());

            Optional<JobExecution> jobExecutionOpt = runningJobExecutions.stream().findFirst();

            // 上次job执行状态，如正在执行中则跳过，如执行完成则根据cron表达式决定是否要执行job
            ExitStatus taskStatus = null;

            // 如果存在执行记录
            if (jobExecutionOpt.isPresent()) {

                JobExecution jobExecution = jobExecutionOpt.get();

                taskStatus = getTaskStatus(jobExecution.getJobId());
            }
            // 如果此job从未执行过
            else {
                // do nothing
            }

            // 如果job正在执行中，跳过此次调度
            if (ExitStatus.EXECUTING.equals(taskStatus)) {
                continue;
            }

            // jobs下次执行时间
            DateTime nextExecuteTime = jobExecutionMap.get(jobConfig.getJobName());

            // 如果下次执行时间不存在时，说明此job为第一次执行，调度job执行。
            // 如果下次执行时间在当前时间之前或者等于当前时间时，调度job执行。
            if (Objects.isNull(nextExecuteTime) || nextExecuteTime.isBeforeNow() || nextExecuteTime.isEqualNow()) {

                try {

                    jobOperator.start(jobConfig.getJobName(), UUID.randomUUID().toString());

                    DateTime nextExecutionTime = CrontabHelper
                            .nextExecutionTime(jobConfig.getCron());

                    jobExecutionMap.put(jobConfig.getJobName(), nextExecutionTime);
                } catch (Exception e) {
                    log.error("error caused by:", e);
                }
            }
        }
    }

    /**
     * 根据executionId获取job运行状态
     *
     * @param executionId
     * @return
     */
    private ExitStatus getTaskStatus(Long executionId) {

        ExitStatus taskStatus = null;

        if (Objects.nonNull(executionId)) {

            org.springframework.batch.core.JobExecution jobExecution = jobExplorer
                    .getJobExecution(executionId);

            if (Objects.nonNull(jobExecution)) {
                taskStatus = jobExecution.getExitStatus();
            }

        }

        if (Objects.isNull(taskStatus)) {
            // 当该execute id的状态未知的时候，设置运行状态为完成
            taskStatus = ExitStatus.COMPLETED;
        }

        return taskStatus;
    }


}
