package com.drizzle.job.scheduler.springbatch.jobs.demo.step;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description:
 * @Author Drizzle
 * @Date 2021/1/17 16:51
 */
public class TestChunkReader extends CommonReader<String> {

    @Override
    protected List<String> paging(Integer offset, Integer limit) throws Exception {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            Random random = new Random();

            list.add(i + "" + random.nextInt());
        }

        return list;
    }
}