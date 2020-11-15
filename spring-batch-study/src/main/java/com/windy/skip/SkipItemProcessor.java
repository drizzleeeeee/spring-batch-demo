package com.windy.skip;

import com.windy.retry.CustomerRetryException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @description: xxx
 * @time: 2020-04-18 12:35
 * @author: baojinlong
 **/
@Component
public class SkipItemProcessor implements ItemProcessor<String, String> {
    private int attemptCount = 0;


    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item " + item);
        if ("26".equals(item)) {
            throw new CustomerRetryException("Process failed of item 26");
        } else {
            return String.valueOf(Integer.parseInt(item) * -1);
        }
    }
}
