package com.windy.itemreaderdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description: xxx
 * @time: 2020-04-10 09:23
 * @author: baojinlong
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private int age;
    private Long currentTime;
    private String message;
}
