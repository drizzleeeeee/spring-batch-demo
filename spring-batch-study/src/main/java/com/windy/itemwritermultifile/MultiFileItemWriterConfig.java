package com.windy.itemwritermultifile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windy.itemreaderfile.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.*;

/**
 * @description: xxx
 * @time: 2020-04-17 15:11
 * @author: baojinlong
 **/
@Configuration
@Slf4j
public class MultiFileItemWriterConfig {
    /**
     * 也可以直接注入,因为在bean中已经有了.
     *
     * @return
     * @throws Exception
     */
    @Bean
    public FlatFileItemWriter<Customer> jsonItemWriter3() throws Exception {
        // 把Customer对象转成字符串输出到文件
        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriter<>();
        String path = "e:/customer2222-ou.json";
        flatFileItemWriter.setResource(new FileSystemResource(path));
        // 将对象转成字符串
        flatFileItemWriter.setLineAggregator(new LineAggregator<Customer>() {
            ObjectMapper objectMapper = new ObjectMapper();
            String strResult = null;

            @Override
            public String aggregate(Customer customer) {
                try {
                    strResult = objectMapper.writeValueAsString(customer);
                } catch (JsonProcessingException e) {
                    log.error("将对象转成字符串出现异常");
                }
                return strResult;
            }
        });
        // 检查
        flatFileItemWriter.afterPropertiesSet();
        return flatFileItemWriter;
    }

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter3() throws Exception {
        StaxEventItemWriter<Customer> staxEventItemWriter = new StaxEventItemWriter<>();
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        Map<String, Class<Customer>> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        xStreamMarshaller.setAliases(aliases);
        staxEventItemWriter.setMarshaller(xStreamMarshaller);
        staxEventItemWriter.setRootTagName("customers");
        // 写入文件位置
        String path = "e:/customers222-qi.xml";
        staxEventItemWriter.setResource(new FileSystemResource(path));
        staxEventItemWriter.afterPropertiesSet();
        return staxEventItemWriter;
    }

    /**
     * 输出数据到多个文件,不进行分类
     *
     * @return
     * @throws Exception
     */
//    @Bean
//    public CompositeItemWriter<Customer> multiFileItemWriterNoClassification() throws Exception {
//        CompositeItemWriter<Customer> returnResult = new CompositeItemWriter<>();
//        returnResult.setDelegates(Arrays.asList(xmlItemWriter3(), jsonItemWriter3()));
//        returnResult.afterPropertiesSet();
//        return returnResult;
//    }

    /**
     * 输出数据到多个文件,进行分类如性别男/女分别输出到不同的文件中
     *
     * @return
     * @throws Exception
     */
    @Bean
    public ClassifierCompositeItemWriter<Customer> multiFileItemWriterNoClassification() throws Exception {
        ClassifierCompositeItemWriter<Customer> classifierCompositeItemWriter = new ClassifierCompositeItemWriter<>();
        classifierCompositeItemWriter.setClassifier((Classifier<Customer, ItemWriter<? super Customer>>) customer -> {
            ItemWriter<Customer> writer = null;
            int id = customer.getId();
            try {
                if (id % 2 == 0) {
                    writer = xmlItemWriter3();
                } else {
                    writer = jsonItemWriter3();
                }
            } catch (Exception e) {
                log.error("分类出现异常", e);
            }
            return writer;
        });
        return classifierCompositeItemWriter;
    }
}
