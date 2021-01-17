package com.windy.config;

import com.windy.constant.SpringBatchConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * @description: xxx
 * @time: 2020-04-08 13:32
 * @author: baojinlong
 **/
public class MyOwnJobExecutionDecider implements JobExecutionDecider {
    private int count;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if (count % 2 == 0) {
            return new FlowExecutionStatus(SpringBatchConstants.EVEN);
        } else {
            return new FlowExecutionStatus(SpringBatchConstants.ODD);
        }
    }
}
