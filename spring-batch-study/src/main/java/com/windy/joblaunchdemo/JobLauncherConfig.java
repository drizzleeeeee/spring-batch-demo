package com.windy.joblaunchdemo;

import com.windy.skiplistener.MySkipListener;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description: xxx
 * @time: 2020-04-18 13:02
 * @author: baojinlong
 **/
@Configuration
public class JobLauncherConfig implements StepExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private Map<String, JobParameter> parameters;

    @Bean
    public Job jobLauncherDemoJob() {
        return jobBuilderFactory.get("jobLauncherDemoJob")
                .start(jobLauncherDemoStep())
                .build();
    }

    private Step jobLauncherDemoStep() {
        return stepBuilderFactory.get("jobLauncherDemoStep")
                .listener(this)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("任务接收到参数为" + parameters.get("message").getValue());
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("JobLauncherDemo.beforeStep");
        JobParameters jobParameters = stepExecution.getJobParameters();
        parameters = jobParameters.getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("JobLauncherDemo.afterStep");
        return null;
    }
}
