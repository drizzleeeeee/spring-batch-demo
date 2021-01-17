package com.windy.itermreadermulti;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-13 22:18
 * @author: baojinlong
 **/
@Component
public class MultiFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        list.forEach(System.out::println);
    }
}
