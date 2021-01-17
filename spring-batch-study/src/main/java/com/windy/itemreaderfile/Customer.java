package com.windy.itemreaderfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: xxx
 * @time: 2020-04-10 09:56
 * @author: baojinlong
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String birthday;
}
