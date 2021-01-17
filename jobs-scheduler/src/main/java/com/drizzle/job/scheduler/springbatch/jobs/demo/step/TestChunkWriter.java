package com.drizzle.job.scheduler.springbatch.jobs.demo.step;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @Description:
 * @Author Drizzle
 * @Date 2021/1/17 16:51
 */
public class TestChunkWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {

        System.out.println(items);

    }
}