package com.windy.itemwriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-16 15:16
 * @author: baojinlong
 **/
@Configuration
public class ItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("myWriter")
    private MyWriter myWriter;

    @Bean
    public Job itemWriterJob() {
        return jobBuilderFactory.get("ItemWriterJob")
                .start(itemWriterDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDemoStep() {
        return stepBuilderFactory.get("itemWriterDemoStep")
                .<String, String>chunk(2)
                .reader(myReader())
                .writer(myWriter)
                .build();
    }

    @Bean
    public ItemReader<? extends String> myReader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            items.add("java" + i);
        }
        return new ListItemReader<>(items);
    }
}
