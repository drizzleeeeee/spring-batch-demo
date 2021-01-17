package com.windy.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 06 job嵌套-子job 本job包含一个step
 * @time: 2020-04-08 13:48
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class ChildJob1 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step childJob1Step1() {
        return stepBuilderFactory.get("childJob1Step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是childJob1Step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job childJobOne() {
        return jobBuilderFactory.get("childJobOne").start(childJob1Step1()).build();
    }
}
