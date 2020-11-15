package com.windy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring-batch入门级案例
 *
 * @author baojinlong
 */
@SpringBootApplication
@EnableBatchProcessing
public class MySpringBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBatchApplication.class, args);
    }
}
