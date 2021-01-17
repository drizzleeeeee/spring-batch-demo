package com.windy.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @description: 07 job嵌套整合,父job包含两个子job,每个子job下面分别包含一个和两个step
 * @time: 2020-04-08 13:48
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class NestedDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Job childJobOne;
    @Autowired
    private Job childJobTwo;
    @Autowired
    private JobLauncher jobLauncher;

    /**
     * 可以使用springboot 类似@JobRepository jobRepository等注解
     *
     * @param jobRepository
     * @param platformTransactionManager
     * @return
     */
    @Bean
    public Job myParentJobs3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return jobBuilderFactory.get("myParentJobs3")
                .start(childJob1(jobRepository, platformTransactionManager))
                .next(childJob2(jobRepository, platformTransactionManager))
                .build();
    }

    /**
     * 返回的是Job类型的Step,特殊的Step
     *
     * @return
     */
    private Step childJob2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        // 使用启动父Job的启动对象
        return new JobStepBuilder(new StepBuilder("childJobTwo"))
                .job(childJobTwo)
                // 使用启动父job的启动对象来启动子job
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();
    }

    private Step childJob1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        // 使用启动父Job的启动对象
        return new JobStepBuilder(new StepBuilder("childJobOne"))
                .job(childJobOne)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();
    }
}
