package com.windy.itemwriterxml;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxx
 * @time: 2020-04-17 14:36
 * @author: baojinlong
 **/
@Configuration
public class XmlItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("dbJdbcReader2File")
    private ItemReader<? extends Customer> dbJdbcReader;
    @Autowired
    private ItemWriter<? super Customer> xmlItemWriter;

    @Bean
    public Job xmlItemWriterDemoJob() {
        return jobBuilderFactory.get("xmlItemWriterDemoJob")
                .start(xmlItemWriterStep())
                .build();
    }

    @Bean
    public Step xmlItemWriterStep() {
        return stepBuilderFactory.get("xmlItemWriterStep")
                .<Customer, Customer>chunk(10)
                .reader(dbJdbcReader)
                .writer(xmlItemWriter)
                .build();
    }
}
