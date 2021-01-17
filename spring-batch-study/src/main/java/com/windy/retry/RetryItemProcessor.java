package com.windy.retry;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @description: 错误处理, 不希望任务被中断
 * @time: 2020-04-18 12:09
 * @author: baojinlong
 **/
@Component
public class RetryItemProcessor implements ItemProcessor<String, String> {
    private int attemptCount = 0;

    @Override
    public String process(String item) throws Exception {
        System.out.println("处理到item=" + item);
        if ("26".equals(item)) {
            attemptCount++;
            if (attemptCount >= 3) {
                System.out.println("Retried " + attemptCount + " times success.");
                return String.valueOf(Integer.parseInt(item) * -1);

            } else {
                System.out.println("processed the " + attemptCount + "times fails");
                throw new CustomerRetryException("process failed. Attempt: " + attemptCount);
            }
        } else {
            return String.valueOf(Integer.parseInt(item) * -1);
        }
    }
}
