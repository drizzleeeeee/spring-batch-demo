package com.windy.itemreaderdb;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-10 09:38
 * @author: baojinlong
 **/
@Component("myNewDbJdbcWriter")
public class MyNewDbJdbcWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> list) throws Exception {
        list.forEach(System.out::println);
    }
}
