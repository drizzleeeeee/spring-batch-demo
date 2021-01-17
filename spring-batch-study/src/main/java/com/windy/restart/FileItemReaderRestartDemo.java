package com.windy.restart;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 失败后重新启动后自动从错误位置执行
 * @time: 2020-04-13 22:38
 * @author: baojinlong
 **/
@Configuration
public class FileItemReaderRestartDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("restartReader")
    private ItemReader<Customer> restartReader;
    @Autowired
    private ItemWriter<Customer> exceptionFlatFileWriter;

    @Bean
    public Job restartJobDemo() {
        return jobBuilderFactory.get("restartJobDemo")
                .start(restartStepDemo()).build();
    }

    @Bean
    public Step restartStepDemo() {
        return stepBuilderFactory.get("restartStepDemo")
                // 每次读取一百个
                .<Customer, Customer>chunk(10)
                .reader(restartReader)
                .writer(exceptionFlatFileWriter)
                .build();
    }
}