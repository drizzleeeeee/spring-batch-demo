package com.windy.itemreaderxml;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 从xml文件中读取文件
 * @time: 2020-04-13 16:53
 * @author: baojinlong
 **/
@Component
public class XmlFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        list.forEach(System.out::println);
    }
}
