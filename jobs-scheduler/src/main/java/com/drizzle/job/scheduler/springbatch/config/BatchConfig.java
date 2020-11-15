package com.drizzle.job.scheduler.springbatch.config;//package name.ealen.batch;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//为了使Spring Batch使用基于Map的JobRepository，我们需要扩展DefaultBatchConfigurer。
// 重写setDataSource()方法以不设置DataSource。这将导致自动配置使用基于Map的JobRepository。
public class BatchConfig extends DefaultBatchConfigurer {
    @Override
    public void setDataSource(DataSource dataSource) {
        // initialize will use a Map based JobRepository (instead of database)
        System.out.println("hhahah");
    }
}
