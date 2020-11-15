package com.drizzle.job.scheduler.springbatch.config;

import com.drizzle.job.scheduler.springbatch.config.properties.JobSchedulerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 调度器配置类
 */
@Configuration
@ComponentScan("com.drizzle.job.scheduler.springbatch")
@EnableConfigurationProperties(JobSchedulerProperties.class)
public class SchedulerConfig {
}
