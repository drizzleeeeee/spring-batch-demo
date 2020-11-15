package com.windy.itemwriterdb;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxx
 * @time: 2020-04-16 15:51
 * @author: baojinlong
 **/
@Configuration
public class ItemWriterDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("myFlatFileReaderSb")
    private ItemReader<? extends Customer> flatFileReader;
    @Qualifier("myItemWriterDb")
    @Autowired
    private ItemWriter<? super Customer> myItemWriterDb;

    @Bean
    public Job itemWriterDbDemoJob() {
        return jobBuilderFactory.get("itemWriterDbDemoJob")
                .start(myItemWriterDbDemoStep()).
                        build();
    }

    private Step myItemWriterDbDemoStep() {
        return stepBuilderFactory.get("myItemWriterDbDemoStep")
                .<Customer, Customer>chunk(10)
                .reader(flatFileReader)
                .writer(myItemWriterDb)
                .build();
    }

}
