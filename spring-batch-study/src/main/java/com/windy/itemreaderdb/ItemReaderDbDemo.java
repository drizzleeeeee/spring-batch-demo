package com.windy.itemreaderdb;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: xxx
 * @time: 2020-04-10 09:24
 * @author: baojinlong
 **/
@Configuration
public class ItemReaderDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    @Qualifier("myNewDbJdbcWriter")
    private ItemWriter<? super User> myNewDbJdbcWriter;


    @Bean
    public Job itemReaderDbJob() {
        return jobBuilderFactory.get("itemReaderDbJob").start(itemReaderDbStep()).build();
    }

    @Bean
    public Step itemReaderDbStep() {
        return stepBuilderFactory.get("itemReaderDbStep")
                // 读取都是User类型
                .<User, User>chunk(2)
                .reader(dbJdbcReader())
                .writer(myNewDbJdbcWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<User> dbJdbcReader() {
        // 从数据库中读取数据
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // 一次读取两条
        reader.setFetchSize(2);
        // 把读取到的数据转user对象
        reader.setRowMapper((resultSet, rowNum) -> {
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setUsername(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setAge(resultSet.getInt(4));
            return user;
        });
        // 指定sql语句
        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("id,username,password,age");
        mySqlPagingQueryProvider.setFromClause("from user");
        // 指定排序
        Map<String, Order> sortKeys = new HashMap<>(2);
        sortKeys.put("id", Order.DESCENDING);
        mySqlPagingQueryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(mySqlPagingQueryProvider);
        return reader;
    }

}
