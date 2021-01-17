package com.windy.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @description: 04并发执行, 执行顺序不会按照指定的顺序
 * @time: 2020-04-08 09:53
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class SplitDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step splitDemoStep01() {
        return stepBuilderFactory.get("splitDemoStep01").tasklet((stepContribution, chunkContext) -> {
            System.out.println("splitDemoStep01");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step splitDemoStep02() {
        return stepBuilderFactory.get("splitDemoStep02").tasklet((stepContribution, chunkContext) -> {
            System.out.println("splitDemoStep02");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step splitDemoStep03() {
        return stepBuilderFactory.get("splitDemoStep03").tasklet((stepContribution, chunkContext) -> {
            System.out.println("splitDemoStep03");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Flow splitDemoFlow1() {
        return new FlowBuilder<Flow>("splitDemoFlow1").start(splitDemoStep01()).build();
    }

    @Bean
    public Flow splitDemoFlow2() {
        return new FlowBuilder<Flow>("splitDemoFlow2").start(splitDemoStep02()).next(splitDemoStep03()).build();
    }

    /**
     * 创建任务,包含两个flow并且并发执行
     */
    @Bean
    public Job splitJobDemo() {
        // splitDemoStep03在一个线程里面  splitDemoStep01 splitDemoStep02在一个线程里面,但是两者依然存在竞争关系
        return jobBuilderFactory.get("splitJobDemo").start(splitDemoFlow1()).split(new SimpleAsyncTaskExecutor()).add(splitDemoFlow2()).end().build();
    }
}
