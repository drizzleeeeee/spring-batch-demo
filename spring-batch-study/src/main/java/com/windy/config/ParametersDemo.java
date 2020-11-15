package com.windy.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description: job参数案例
 * @time: 2020-04-09 09:35
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private Map<String, JobParameter> parameters;

    @Bean
    public Job parameterJob() {
        return jobBuilderFactory.get("parameterJob").start(parameterStep()).build();
    }

    @Bean
    public Step parameterStep() {
        // job执行的是step,job使用的数据肯定是在step中使用,只需要给step传递数据参数,使用监听就可以,直接使用当前类作为监听
        return stepBuilderFactory.get("parameterStep")
                .listener(this)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("接收到参数的值是" + parameters.getOrDefault("name", new JobParameter("name")));
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // 当step执行的时候可以获取到参数值,在运行时候指定参数info=windy
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
