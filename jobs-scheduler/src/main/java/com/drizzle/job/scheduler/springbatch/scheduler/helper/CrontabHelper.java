package com.drizzle.job.scheduler.springbatch.scheduler.helper;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Date;

public class CrontabHelper {

    /**
     * 根据cron表达式获取下次执行时间
     *
     * @param cron
     * @return
     */
    public static DateTime nextExecutionTime(String cron) {

        if (StringUtils.isBlank(cron)) {
            return null;
        }

        CronTrigger trigger = new CronTrigger(cron);
        TriggerContext context = new SimpleTriggerContext();
        Date nextExecutionTime = trigger.nextExecutionTime(context);

        return new DateTime(nextExecutionTime.getTime());

    }
}
