package com.windy.itemprocessor;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxx
 * @time: 2020-04-17 16:44
 * @author: baojinlong
 **/
@Configuration
public class IdFilterProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        if (item.getId() % 2 == 0) {
            return item;
        } else {
            // 相当于把对象过滤掉
            return null;
        }
    }
}
