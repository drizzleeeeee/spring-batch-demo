package com.windy.itemwriterfile;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 向文件中输出
 * @time: 2020-04-17 11:30
 * @author: baojinlong
 **/
@Configuration
public class FileItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<? extends Customer> dbJdbcReader2File;
    @Autowired
    private ItemWriter<? super Customer> fileItemWriter;

    @Bean
    public Job fileItemWriterDemoJob2() {
        return jobBuilderFactory.get("fileItemWriterDemoJob2")
                .start(fileItemWriterDemoStep2())
                .build();
    }

    @Bean
    public Step fileItemWriterDemoStep2() {
        return stepBuilderFactory.get("fileItemWriterDemoStep2")
                .<Customer, Customer>chunk(10)
                .reader(dbJdbcReader2File)
                .writer(fileItemWriter)
                .build();
    }
}
