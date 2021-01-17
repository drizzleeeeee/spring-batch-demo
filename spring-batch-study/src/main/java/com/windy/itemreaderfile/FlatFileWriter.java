package com.windy.itemreaderfile;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @description: 解析本地文件
 * @time: 2020-04-13 15:41
 * @author: baojinlong
 **/
@Component("flatFileWriter")
public class FlatFileWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        list.forEach(System.out::println);
    }
}
