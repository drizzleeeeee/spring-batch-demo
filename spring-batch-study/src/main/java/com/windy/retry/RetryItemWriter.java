package com.windy.retry;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-18 12:27
 * @author: baojinlong
 **/
@Component
public class RetryItemWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println(items);
    }
}
