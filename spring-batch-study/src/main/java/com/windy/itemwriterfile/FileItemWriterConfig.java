package com.windy.itemwriterfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windy.itemreaderfile.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @description: xxx
 * @time: 2020-04-17 11:41
 * @author: baojinlong
 **/
@Configuration
@Slf4j
public class FileItemWriterConfig {
    @Bean
    public FlatFileItemWriter<Customer> fileItemWriter() throws Exception {
        // 把Customer对象转成字符串输出到文件
        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriter<>();
        String path = "e:/customer2222888.txt";
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
}
