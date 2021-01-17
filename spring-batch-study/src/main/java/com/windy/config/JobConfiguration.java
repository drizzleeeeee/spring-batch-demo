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
 * @description: 02
 * @time: 2020-04-07 23:36
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class JobConfiguration {
    /**
     * 注入创建任务对象
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    /**
     * 注入创建Step对象
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // 创建任务对象
    @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("helloWorldJob").start(step1()).build();
    }

    private Step step1() {
        // 还可以用chunk
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("我是spring-batch.hello world.");
            // 没有下一步骤直接返回完成
            return RepeatStatus.FINISHED;
        }).build();
    }
}
