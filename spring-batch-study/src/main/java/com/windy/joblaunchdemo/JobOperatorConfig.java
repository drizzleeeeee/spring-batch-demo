package com.windy.joblaunchdemo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description: 通过StepExecutionListener这个可以获取参数值
 * @time: 2020-04-18 13:28
 * @author: baojinlong
 **/
@Configuration
public class JobOperatorConfig implements StepExecutionListener, ApplicationContextAware {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private Map<String, JobParameter> parameters;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;

    /**
     * 将任务的名称和bean对象关联起来的对象,需要提前创建该bean jobRegistryBeanPostProcessor()
     */
    @Autowired
    private JobRegistry jobRegistry;
    private ApplicationContext applicationContext;
    @Autowired
    private JobExplorer jobExplorer;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        jobRegistryBeanPostProcessor.setBeanFactory(applicationContext);
        jobRegistryBeanPostProcessor.afterPropertiesSet();
        return jobRegistryBeanPostProcessor;
    }

    /**
     * 自己创建该对象,必须
     *
     * @return
     */
    @Bean
    public JobOperator jobOperator() {
        SimpleJobOperator simpleJobOperator = new SimpleJobOperator();
        simpleJobOperator.setJobLauncher(jobLauncher);
        // 设置参数转化器,因为传过来参数形式为key=value
        simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
        simpleJobOperator.setJobRepository(jobRepository);
        simpleJobOperator.setJobRegistry(jobRegistry);
        simpleJobOperator.setJobExplorer(jobExplorer);
        return simpleJobOperator;
    }

    @Bean
    public Job jobOperatorDemoJob() {
        System.out.println("JobOperatorConfig.jobOperatorDemoJob");
        return jobBuilderFactory.get("jobOperatorDemoJob")
                .start(jobOperatorDemoStep())
                .build();
    }

    private Step jobOperatorDemoStep() {
        return stepBuilderFactory.get("jobLauncherDemoStep")
                .listener(this)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("任务接收到参数为" + parameters.get("message").getValue());
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("JobOperatorConfig.beforeStep");
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("JobOperatorConfig.afterStep");
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
