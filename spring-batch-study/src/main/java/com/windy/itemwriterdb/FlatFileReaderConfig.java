package com.windy.itemwriterdb;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @description: xxx
 * @time: 2020-04-17 10:25
 * @author: baojinlong
 **/
@Configuration
public class FlatFileReaderConfig {
    /**
     * 从文件中读取
     *
     * @return
     */
    @Bean
    public FlatFileItemReader<Customer> myFlatFileReaderSb() {
        // 创建该对象
        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        // 设置文件位置
        flatFileItemReader.setResource(new ClassPathResource("customers.txt"));
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
