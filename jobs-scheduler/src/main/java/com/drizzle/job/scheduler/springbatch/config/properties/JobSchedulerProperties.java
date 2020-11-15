package com.drizzle.job.scheduler.springbatch.config.properties;

import com.drizzle.job.scheduler.springbatch.scheduler.model.JobConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Administrator
 * @Date 2020/11/8 19:55
 */
@ConfigurationProperties(prefix = JobSchedulerProperties.PREFIX)
public class JobSchedulerProperties {

    public static final String PREFIX = "job.scheduler";

    /**
     * 要执行的job列表
     */
    private List<JobConfig> jobConfigList;

    public List<JobConfig> getJobConfigList() {
        return jobConfigList;
    }

    public void setJobConfigList(List<JobConfig> jobConfigList) {
        this.jobConfigList = jobConfigList;
    }
}
