package com.windy.itemwritermultifile;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 从数据库中读取并写出到不同文件
 * @time: 2020-04-17 15:01
 * @author: baojinlong
 **/
@Configuration
public class ItemWriterMultiFileDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("dbJdbcReader2File")
    private ItemReader<? extends Customer> dbJdbcReader;
    @Autowired
    @Qualifier("multiFileItemWriterNoClassification")
    private ItemWriter<? super Customer> multiFileItemWriter;
    @Autowired
    @Qualifier("jsonItemWriter3")
    private ItemStreamWriter<Customer> fileWriter;
    @Autowired
    @Qualifier("xmlItemWriter3")
    private ItemStreamWriter<Customer> jsonFileWriter;


    @Bean
    public Job multiFileItemWriterDemoJob() {
        return jobBuilderFactory.get("multiFileItemWriterDemoJob23")
                .start(multiFileItemWriterDemoStep())
                .build();
    }

    @Bean
    public Step multiFileItemWriterDemoStep() {
        return stepBuilderFactory.get("multiFileItemWriterDemoStep33")
                .<Customer, Customer>chunk(10)
                .reader(dbJdbcReader)
                .writer(multiFileItemWriter)
                .stream(jsonFileWriter)
                .stream(fileWriter)
                .build();
    }
}
