package com.windy.itemreader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

/**
 * @description: xxx
 * @time: 2020-04-09 10:03
 * @author: baojinlong
 **/
public class MyItemReaderImpl implements ItemReader<String> {
    private Iterator<String> iterator;

    public MyItemReaderImpl(List<String> data) {
        this.iterator = data.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iterator.hasNext()) {
            // 一个一个读取
            return this.iterator.next();
        } else {
            return null;
        }
    }
}
