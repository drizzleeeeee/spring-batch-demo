package com.windy.itemprocessor;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 读取数据并处理数据后保存到文件
 * @time: 2020-04-17 15:57
 * @author: baojinlong
 **/
@Configuration
public class ItemProcessorDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("dbJdbcReader2File")
    private ItemReader<? extends Customer> dbJdbcReader;
    /**
     * 在com.windy.itemwriterfile.FileItemWriterConfig定义了
     */
    @Autowired
    @Qualifier("fileItemWriter")
    private ItemWriter<Customer> dbFileWriter;

    @Autowired
    @Qualifier("firstNameUpperProcessorConfig")
    private ItemProcessor<Customer, Customer> firstNameUpperProcessor;

    @Autowired
    @Qualifier("idFilterProcessor")
    private ItemProcessor<Customer, Customer> idFilterProcessor;

    @Bean
    public Job myItemProcessorDemoJob() {
        return jobBuilderFactory.get("myItemProcessorDemoJobMultiProcess")
                .start(itemProcessorDemoStep())
                .build();
    }


    @Bean
    public Step itemProcessorDemoStep() {
        return stepBuilderFactory.get("itemProcessorDemoStep")
                .<Customer, Customer>chunk(10)
                .reader(dbJdbcReader)
                .processor(multiProcessor())  // 单个用firstNameUpperProcessor
                .writer(dbFileWriter)
                .build();

    }

    /**
     * 如果有多个处理器则使用CompositeItemProcessor
     *
     * @return
     */
    @Bean
    public CompositeItemProcessor<Customer, Customer> multiProcessor() {
        CompositeItemProcessor<Customer, Customer> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<Customer, Customer>> delegates = new ArrayList<>();
        delegates.add(firstNameUpperProcessor);
        delegates.add(idFilterProcessor);
        processor.setDelegates(delegates);
        return processor;
    }

}
