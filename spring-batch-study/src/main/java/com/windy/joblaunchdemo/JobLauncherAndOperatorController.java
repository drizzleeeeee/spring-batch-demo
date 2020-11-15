package com.windy.joblaunchdemo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.windy.itemreaderdb.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 统一调度, 使用JobLauncher/JobOperator
 * @time: 2020-04-18 13:07
 * @author: baojinlong
 **/
@RestController
public class JobLauncherAndOperatorController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job jobLauncherDemoJob;
    @Autowired
    private JobOperator jobOperator;

    @RequestMapping("/job/{message}")
    public String jobRun1(@PathVariable String message) throws Exception {
        System.out.println("接收到前端参数为" + message);
        // 把参数的值传递给将要执行的任务
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("inputDate", new Date());
        jobParametersBuilder.addString("message", message);
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        // 启动任务,并把参数传递给任务
        jobLauncher.run(jobLauncherDemoJob, jobParameters);
        return "job launcher success.";
    }


    @RequestMapping("/job2/{message}")
    public Map<String, Object> jobRun2(@PathVariable String message) throws Exception {
        System.out.println("接收到前端参数为" + message);
        // 后面参数是固定写法key='value' jobOperatorDemoJob名字需要和JobOperatorConfig.jobOperatorDemoJob方法中一致
        // 拼接参数
        User user = new User();
        user.setMessage(message);
        user.setAge(22);
        user.setUsername("windy");
        user.setId(2);
        user.setCurrentTime(System.nanoTime());
        //jackson的核心，通过mapper来进行序列化和反序列化,带逗号参数接收不到全部
        String inputParam = JSON.toJSONString(user);
        System.out.println("inputParam = " + inputParam);
        inputParam = inputParam.replace(",", " ");
        jobOperator.start("jobOperatorDemoJob", "message=" + inputParam);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("data", "ok");
        return returnMap;
    }
}
