package com.drizzle.job.scheduler.springbatch.jobs.demo.step;


import org.springframework.batch.item.ItemProcessor;

/**
 * @Description:
 * @Author Drizzle
 * @Date 2021/1/17 16:51
 */
public class TestChunkProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        return item.toUpperCase() + "【Processed】";
    }
}