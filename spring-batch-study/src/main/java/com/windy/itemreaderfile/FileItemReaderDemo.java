package com.windy.itemreaderfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * @description: 从文件中读取
 * @time: 2020-04-10 09:57
 * @author: baojinlong
 **/
@Configuration
public class FileItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemWriter<Customer> flatFileWriter;

    @Bean
    public Job fileItemReaderDemoJob() {
        return jobBuilderFactory.get("fileItemReaderDemoJob").start(fileItemReaderDemoStep()).build();
    }

    @Bean
    public Step fileItemReaderDemoStep() {
        return stepBuilderFactory.get("fileItemReaderDemoStep")
                // 每次读取一百个
                .<Customer, Customer>chunk(10)
                .reader(flatFileReader())
                .writer(flatFileWriter)
                .build();
    }

    /**
     * 从文件中读取
     *
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> flatFileReader() {
        // 创建该对象
        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        // 设置文件位置
        flatFileItemReader.setResource(new ClassPathResource("customer.txt"));
        // 跳过第一行
        flatFileItemReader.setLinesToSkip(1);
        // 从第二行进行数据解析
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("id", "firstName", "lastName", "birthday");
        // 将解析出来的数据映射为一个对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(delimitedLineTokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            // 创建最终对象
            Customer customer = new Customer();
            customer.setId(fieldSet.readInt("id"));
            customer.setFirstName(fieldSet.readString("firstName"));
            customer.setLastName(fieldSet.readString("lastName"));
            customer.setBirthday(fieldSet.readString("birthday"));
            return customer;
        });
        // 检查下结果集
        mapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(mapper);
        return flatFileItemReader;
    }
}
