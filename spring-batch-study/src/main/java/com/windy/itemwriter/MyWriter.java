package com.windy.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 写数据处理类
 * @time: 2020-04-16 14:52
 * @author: baojinlong
 **/
@Component("myWriter")
public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        System.out.println(list.size());
        list.forEach(System.out::println);
    }
}
