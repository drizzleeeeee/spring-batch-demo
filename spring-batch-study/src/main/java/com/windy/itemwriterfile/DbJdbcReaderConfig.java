package com.windy.itemwriterfile;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: xxx
 * @time: 2020-04-17 11:34
 * @author: baojinlong
 **/
@Configuration
public class DbJdbcReaderConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<Customer> dbJdbcReader2File() {
        // 从数据库中读取数据
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // 一次读取两条
        reader.setFetchSize(10);
        // 把读取到的数据转user对象
        reader.setRowMapper((resultSet, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(resultSet.getInt(1));
            customer.setFirstName(resultSet.getString(2));
            customer.setLastName(resultSet.getString(3));
            customer.setBirthday(resultSet.getString(4));
            return customer;
        });
        // 指定sql语句
        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("id,firstName,lastName,birthday");
        mySqlPagingQueryProvider.setFromClause("from customer");
        // 指定排序
        Map<String, Order> sortKeys = new HashMap<>(2);
        sortKeys.put("id", Order.DESCENDING);
        mySqlPagingQueryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(mySqlPagingQueryProvider);
        return reader;
    }
}
