package com.windy.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 06 job嵌套 本job包含两个step
 * @time: 2020-04-08 13:48
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class ChildJob2 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step childJob2Step1() {
        return stepBuilderFactory.get("childJob2Step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是childJob2Step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step childJob2Step2() {
        return stepBuilderFactory.get("childJob2Step2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是childJob2Step2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job childJobTwo() {
        return jobBuilderFactory.get("childJobTwo")
                .start(childJob2Step1())
                .next(childJob2Step2())
                .build();
    }
}
