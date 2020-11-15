package com.windy.retry;

/**
 * @description: 自定义重试异常
 * @time: 2020-04-18 12:17
 * @author: baojinlong
 **/
public class CustomerRetryException extends Exception {
    public CustomerRetryException() {
        super();
    }

    public CustomerRetryException(String s) {
        super(s);
    }
}
