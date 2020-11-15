package com.windy.skip;

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
 * @description: 错误跳过
 * @time: 2020-04-18 12:32
 * @author: baojinlong
 **/
@Configuration
public class SkipDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemWriter<String> retryItemWriter;
    @Autowired
    private ItemProcessor<String, String> skipItemProcessor;

    @Bean
    public Job skipDemoJob() {
        return jobBuilderFactory.get("skipDemoJob2")
                .start(skipDemoStep())
                .build();
    }

    private Step skipDemoStep() {
        return stepBuilderFactory.get("skipDemoStep")
                .<String, String>chunk(10)
                .reader(reader())
                .processor(skipItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant()
                .skip(CustomerRetryException.class)
                .skipLimit(5)
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
