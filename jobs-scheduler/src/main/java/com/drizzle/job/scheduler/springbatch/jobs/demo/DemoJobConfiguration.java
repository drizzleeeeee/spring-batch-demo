package com.drizzle.job.scheduler.springbatch.jobs.demo;

import com.drizzle.job.scheduler.springbatch.jobs.demo.step.TestChunkProcessor;
import com.drizzle.job.scheduler.springbatch.jobs.demo.step.TestChunkReader;
import com.drizzle.job.scheduler.springbatch.jobs.demo.step.TestChunkWriter;
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

    public static final String CHUNK_SIZE_JOB_NAME = "chunkSizeJob";
    public static final String CHUNK_SIZE_STEP_NAME = "chunkSizeStep";

    public static final String DEMO_JOB_STEP_NAME = "demoStep";

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

    @Bean
    public Job chunkSizeJob() {

        System.out.println("JobOperatorConfig.chunkSizeJob");

        return jobBuilderFactory.get(CHUNK_SIZE_JOB_NAME)
                .start(chunkSizeStep())
                .build();
    }

    /**
     * 读到chunkSize个item后进行一次writer
     *
     * @return
     */
    @Bean
    public Step chunkSizeStep() {

        TestChunkProcessor testChunkProcessor = new TestChunkProcessor();

        return stepBuilderFactory.get(CHUNK_SIZE_STEP_NAME)
                .<String, String>chunk(95)
                .reader(new TestChunkReader())
                .processor(testChunkProcessor)
                .writer(new TestChunkWriter())
                .build();
    }

}
