package com.windy.config;

import com.windy.constant.SpringBatchConstants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

/**
 * @description: 05决策器使用
 * @time: 2020-04-08 10:12
 * @author: baojinlong
 **/
@EnableBatchProcessing
@Configuration
public class DeciderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step deciderDemoStep01() {
        return stepBuilderFactory.get("deciderDemoStep01").tasklet((stepContribution, chunkContext) -> {
            System.out.println("deciderDemoStep01");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step deciderDemoStep02() {
        return stepBuilderFactory.get("deciderDemoStep02").tasklet((stepContribution, chunkContext) -> {
            System.out.println("deciderDemoStep02");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step deciderDemoStep03() {
        return stepBuilderFactory.get("deciderDemoStep03").tasklet((stepContribution, chunkContext) -> {
            System.out.println("deciderDemoStep03");
            return RepeatStatus.FINISHED;
        }).build();
    }

    /**
     * 创建决策器
     */
    @Bean
    public JobExecutionDecider jobExecutionDecider() {
        return new MyOwnJobExecutionDecider();
    }

    /**
     * 创建任务
     */
    @Bean
    public Job deciderDemoJob() {
        return jobBuilderFactory.get("deciderDemoJob")
                .start(deciderDemoStep01())
                .next(jobExecutionDecider())
                // 表示决策器如果返回even则执行deciderDemoStep02()
                .from(jobExecutionDecider()).on(SpringBatchConstants.EVEN).to(deciderDemoStep02())
                // 表示决策器如果返回odd则执行deciderDemoStep03()
                .from(jobExecutionDecider()).on(SpringBatchConstants.ODD).to(deciderDemoStep03())
                // 执行step03后继续回到决策器才能继续,*表示无论返回啥值
                .from(deciderDemoStep03()).on(SpringBatchConstants.STAR).to(jobExecutionDecider())
                .end().build();

    }
}
