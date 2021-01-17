package com.windy.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * @description: 注解方式实现chunk监听
 * @time: 2020-04-08 23:37
 * @author: baojinlong
 **/
public class MyChunkListener {
    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println("MyChunkListener.beforeChunk" + chunkContext.getStepContext().getStepName());
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println("MyChunkListener.afterChunk" + chunkContext.getStepContext().getStepName());
    }
}
