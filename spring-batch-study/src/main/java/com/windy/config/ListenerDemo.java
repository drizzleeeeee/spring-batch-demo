package com.windy.config;

import com.windy.listener.MyChunkListener;
import com.windy.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @description: 监听器案例
 * @time: 2020-04-08 23:39
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class ListenerDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory.get("listenerJob").start(step01()).listener(new MyJobListener()).build();
    }

    @Bean
    public Step step01() {
        return stepBuilderFactory.get("step01")
                // chunk可以read write process
                .<String, String>chunk(2)
                .faultTolerant()
                .listener(new MyChunkListener())
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemWriter<String> writer() {
        return list -> {
            for (String s : list) {
                System.out.println(s);
            }
        };
    }

    @Bean
    public ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("java", "spring", "scala", "python", "mybatis", "sbWindy"));
    }
}
