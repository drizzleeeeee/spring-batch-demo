package com.drizzle.job.scheduler.springbatch.scheduler.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: job配置实体
 * @Author Administrator
 * @Date 2020/11/8 19:58
 */
@Getter
@Setter
public class JobConfig {

    /**
     * job名称
     */
    private String jobName;

    /**
     * 执行频率
     */
    private String cron;
}
