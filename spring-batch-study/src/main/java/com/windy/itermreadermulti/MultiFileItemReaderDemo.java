package com.windy.itermreadermulti;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @description: 读取多个文件
 * @time: 2020-04-13 21:50
 * @author: baojinlong
 **/
@Configuration
public class MultiFileItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Value("classpath:/file*.txt")
    private Resource[] resourceList;
    @Autowired
    private ItemWriter<Customer> multiFileWriter;

    @Bean
    public Job multiFileItemReaderDemoJob() {
        return jobBuilderFactory.get("multiFileItemReaderDemoJob").
                start(multiFileItemReaderStep())
                .build();
    }

    @Bean
    public Step multiFileItemReaderStep() {
        return stepBuilderFactory.get("multiFileItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(multiFileReader())
                .writer(multiFileWriter)
                .build();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Customer> multiFileReader() {
        MultiResourceItemReader<Customer> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setDelegate(flatFileReader());
        multiResourceItemReader.setResources(resourceList);
        return multiResourceItemReader;
    }

    /**
     * 从文件中读取
     *
     * @return
     */
    @Bean("multiFileBean")
    @StepScope
    public FlatFileItemReader<Customer> flatFileReader() {
        // 创建该对象
        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        // 设置文件位置
        //flatFileItemReader.setResource(new ClassPathResource("customer.txt"));
        // 从第二行进行数据解析
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("id", "firstName", "lastName", "birthday");
        // 将解析出来的数据映射为一个对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(delimitedLineTokenizer);
        mapper.setFieldSetMapper(fieldSet -> {
            // 创建最终对象
            Customer customer = new Customer();
            customer.setId(fieldSet.readInt("id"));
            customer.setFirstName(fieldSet.readString("firstName"));
            customer.setLastName(fieldSet.readString("lastName"));
            customer.setBirthday(fieldSet.readString("birthday"));
            return customer;
        });
        // 检查下结果集
        mapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(mapper);
        return flatFileItemReader;
    }
}
