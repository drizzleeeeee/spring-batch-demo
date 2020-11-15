package com.drizzle.job.scheduler.springbatch.jobs.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 示例Job配置类
 * @Author Administrator
 * @Date 2020/11/15 19:35
 */
@Configuration
public class DemoJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public static final String DEMO_JOB_NAME = "demoJob";

    public static final String DEMO_JOB_STEP_NAME = "demoJob";

    @Bean
    public SimpleJobListener jobListener() {
        return new SimpleJobListener();
    }

    @Bean
    public SimpleStepListener stepListener() {
        return new SimpleStepListener();
    }

    @Bean
    public Job jobOperatorDemoJob() {

        System.out.println("JobOperatorConfig.demoJob");

        return jobBuilderFactory.get(DEMO_JOB_NAME)
                .start(jobOperatorDemoStep())
                .listener(jobListener())
                .build();
    }

    @Bean
    public Step jobOperatorDemoStep() {
        return stepBuilderFactory.get(DEMO_JOB_STEP_NAME)
                .listener(stepListener())
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("do something!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
