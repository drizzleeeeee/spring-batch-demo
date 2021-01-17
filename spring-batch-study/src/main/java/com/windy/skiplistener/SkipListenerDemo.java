package com.windy.skiplistener;

import com.windy.retry.CustomerRetryException;
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
 * @time: 2020-04-18 12:42
 * @author: baojinlong
 **/
@Configuration
public class SkipListenerDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemWriter<String> retryItemWriter;
    @Autowired
    private ItemProcessor<String, String> skipItemProcessor;
    @Autowired
    private MySkipListener mySkipListener;

    @Bean
    public Job skipListenerDemoJob() {
        return jobBuilderFactory.get("skipListenerDemoJob002")
                .start(skipListenerDemoStep())
                .build();
    }

    private Step skipListenerDemoStep() {
        return stepBuilderFactory.get("skipListenerDemoStep")
                .<String, String>chunk(10)
                .reader(reader())
                .processor(skipItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant()
                .skip(CustomerRetryException.class)
                .skipLimit(5)
                .listener(mySkipListener)
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
