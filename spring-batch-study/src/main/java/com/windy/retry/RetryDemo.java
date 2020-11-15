package com.windy.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-18 12:02
 * @author: baojinlong
 **/
@Configuration
public class RetryDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemWriter<String> retryItemWriter;
    @Autowired
    private ItemProcessor<String, String> retryItemProcessor;

    @Bean
    public Job retryDemoJob() {
        return jobBuilderFactory.get("retryDemoJob0dd00")
                .start(retryDemoStep())
                .build();
    }

    private Step retryDemoStep() {
        return stepBuilderFactory.get("retryDemoStep")
                .<String, String>chunk(10)
                .reader(reader())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant()
                .retry(CustomerRetryException.class)
                //重试次数
                .retryLimit(5)
                .build();
    }

    private ItemReader<? extends String> reader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<>(items);
    }
}
