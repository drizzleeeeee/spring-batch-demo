package com.windy.error;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description: xxx
 * @time: 2020-04-17 17:02
 * @author: baojinlong
 **/
@Configuration
public class ErrorDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job errorDemoJob() {
        return jobBuilderFactory.get("errorDemoJo33")
                .start(errorStep1())
                .next(errorStep2())
                .build();
    }

    @Bean
    public Step errorStep1() {
        return stepBuilderFactory.get("errorStep1")
                .tasklet(errorHandling()).build();
    }

    private Tasklet errorHandling() {
        return (contribution, chunkContext) -> {
            Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
            if (stepExecutionContext.containsKey("qianfeng")) {
                System.out.println("the second run will success");
                return RepeatStatus.FINISHED;
            } else {
                // 步骤一二在第一次执行的时候都会报错.运行发生异常了就让其停止
                System.out.println("the first run will fail");
                chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qianfeng", true);
                throw new RuntimeException("need qianfeng value");
            }
        };
    }

    @Bean
    public Step errorStep2() {
        return stepBuilderFactory.get("errorStep2")
                .tasklet(errorHandling()).build();
    }
}
