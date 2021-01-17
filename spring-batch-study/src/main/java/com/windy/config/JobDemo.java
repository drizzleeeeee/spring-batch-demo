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
 * @description: 01
 * @time: 2020-04-07 23:53
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class JobDemo {
    private static final String COMPLETED = "COMPLETED";
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob() {
        // return jobBuilderFactory.get("jobDemoJob").start(step1()).next(step2()).next(step3()).build();
        // 还有诸如 fail stopAndRestart方法
        return jobBuilderFactory.get("jobDemoJob").start(step1()).on(COMPLETED).to(step2()).from(step2()).on(COMPLETED).to(step3()).from(step3()).end().build();
    }

    private Step step3() {
        return stepBuilderFactory.get("step3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是步骤三");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step2() {
        return stepBuilderFactory.get("step2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是步骤二");
            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step step1() {
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是步骤一");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
