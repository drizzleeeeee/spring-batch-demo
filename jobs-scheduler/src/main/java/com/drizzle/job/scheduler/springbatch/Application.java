package com.drizzle.job.scheduler.springbatch;

import com.drizzle.job.scheduler.springbatch.config.SchedulerConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: 项目启动类，测试使用，正常使用调度器应引入调度器配置类 {@link SchedulerConfig}
 * @Author Administrator
 * @Date 2020/11/8 20:09
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableBatchProcessing
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
