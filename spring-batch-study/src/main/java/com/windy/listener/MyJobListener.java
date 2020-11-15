package com.windy.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @description: job层面的监听
 * @time: 2020-04-08 23:36
 * @author: baojinlong
 **/
public class MyJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("MyJobListener.beforeJob" + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("MyJobListener.afterJob" + jobExecution.getJobInstance().getJobName());
    }
}
