package com.windy.itemprocessor;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxx
 * @time: 2020-04-17 16:40
 * @author: baojinlong
 **/
@Configuration
public class FirstNameUpperProcessorConfig implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        /*
         * 将firstName变成大写
         */
        Customer newCustomer = new Customer();
        BeanUtils.copyProperties(item, newCustomer);
        newCustomer.setFirstName(item.getFirstName().toUpperCase());
        return newCustomer;
    }
}
