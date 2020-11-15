package com.windy.itemwriterxml;

import com.windy.itemreaderfile.Customer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: xxx
 * @time: 2020-04-17 14:40
 * @author: baojinlong
 **/
@Configuration
public class XmlItemWriterConfig {
    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {
        StaxEventItemWriter<Customer> staxEventItemWriter = new StaxEventItemWriter<>();
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        Map<String, Class<Customer>> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        xStreamMarshaller.setAliases(aliases);
        staxEventItemWriter.setMarshaller(xStreamMarshaller);
        staxEventItemWriter.setRootTagName("customers");
        // 写入文件位置
        String path = "e:/customers.xml";
        staxEventItemWriter.setResource(new FileSystemResource(path));
        staxEventItemWriter.afterPropertiesSet();
        return staxEventItemWriter;
    }
}
