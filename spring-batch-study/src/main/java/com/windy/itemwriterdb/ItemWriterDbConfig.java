package com.windy.itemwriterdb;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @description: 写到数据库中
 * @time: 2020-04-17 10:29
 * @author: baojinlong
 **/
@Component
public class ItemWriterDbConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Customer> myItemWriterDb() {
        JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql("insert into customer(firstName,lastName,birthday) values(:firstName,:lastName,:birthday)");
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return jdbcBatchItemWriter;
    }
}
