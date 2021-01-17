package com.windy.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 批处理读取文件
 * @time: 2020-04-09 09:55
 * @author: baojinlong
 **/
@Configuration
@EnableBatchProcessing
public class ItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReaderDemoJob() {
        return jobBuilderFactory.get("itemReaderDemoJob")
                .start(itemReaderStep())
                .build();
    }

    @Bean
    public Step itemReaderStep() {
        return stepBuilderFactory.get("itemReaderStep")
                .<String, String>chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list -> {
                    for (String tmp : list) {
                        System.out.println("读取到" + tmp);
                    }
                })
                .build();
    }


    @Bean
    public MyItemReaderImpl itemReaderDemoRead() {
        List<String> data = Arrays.asList("cat", "dog", "pig", "duck");
        return new MyItemReaderImpl(data);
    }
}
