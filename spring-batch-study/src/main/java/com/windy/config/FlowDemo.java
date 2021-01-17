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

/**
 * @description: 03:一个flow由多个step组成并能复用,启动后执行一次就成了完成状态就不再执行了.
 * @time: 2020-04-08 09:41
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class FlowDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * 创建step对象
     *
     * @return
     */
    @Bean
    public Step flowDemoStep1() {
        return stepBuilderFactory.get("flowDemoStep1").tasklet((stepContribution, chunkContext) -> {
            System.out.println("flowDemoStep1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step flowDemoStep2() {
        return stepBuilderFactory.get("flowDemoStep2").tasklet((stepContribution, chunkContext) -> {
            System.out.println("flowDemoStep2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step flowDemoStep3() {
        return stepBuilderFactory.get("flowDemoStep3").tasklet((stepContribution, chunkContext) -> {
            System.out.println("flowDemoStep3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    /**
     * 创建Flow对象,指明flow对象包含哪些step
     *
     * @return
     */
    @Bean
    public Flow flowDemoTest() {
        return new FlowBuilder<Flow>("flowDemoTest").start(flowDemoStep1()).next(flowDemoStep2()).build();
    }

    @Bean
    public Job flowDemoJob() {
        return jobBuilderFactory.get("flowDemoJob").start(flowDemoTest()).next(flowDemoStep3()).end().build();
    }
}
