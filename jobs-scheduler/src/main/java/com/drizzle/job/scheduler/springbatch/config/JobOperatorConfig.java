package com.drizzle.job.scheduler.springbatch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * JobOperator相关配置
 **/
@Configuration
public class JobOperatorConfig implements ApplicationContextAware {

    @Autowired
    private JobRepository jobRepository;

    /**
     * 将任务的名称和bean对象关联起来的对象,需要提前创建该bean jobRegistryBeanPostProcessor()
     */
    @Autowired
    private JobRegistry jobRegistry;

    /**
     * 通过ApplicationContextAware拿到applicationContext
     */
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

    @Bean("asyncJobLauncher")
    public JobLauncher asyncJobLauncher() {

        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();

        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        simpleJobLauncher.setJobRepository(jobRepository);

        return simpleJobLauncher;
    }

    /**
     * 自己创建该对象,必须
     *
     * @return
     */
    @Bean
    public JobOperator jobOperator(@Qualifier("asyncJobLauncher") JobLauncher jobLauncher) {
        SimpleJobOperator simpleJobOperator = new SimpleJobOperator();

        simpleJobOperator.setJobLauncher(jobLauncher);
        // 设置参数转化器,因为传过来参数形式为key=value
        simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
        simpleJobOperator.setJobRepository(jobRepository);
        simpleJobOperator.setJobRegistry(jobRegistry);
        simpleJobOperator.setJobExplorer(jobExplorer);
        return simpleJobOperator;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
