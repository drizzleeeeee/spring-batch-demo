package com.windy.skiplistener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

/**
 * @description: xxx
 * @time: 2020-04-18 12:44
 * @author: baojinlong
 **/
@Component
public class MySkipListener implements SkipListener<String, String> {
    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("MySkipListener.onSkipInRead");
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        System.out.println("MySkipListener.onSkipInWrite");
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        System.out.println(item + " occur exception " + t);
    }
}
