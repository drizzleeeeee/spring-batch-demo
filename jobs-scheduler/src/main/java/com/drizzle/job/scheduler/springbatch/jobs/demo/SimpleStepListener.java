package com.drizzle.job.scheduler.springbatch.jobs.demo;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @Description: 一个简单的Step监听器
 * @Author Administrator
 * @Date 2020/11/15 19:39
 */
public class SimpleStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("demoJob.beforeStep");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("demoJob.afterStep");
        return null;
    }
}
