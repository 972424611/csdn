package com.aekc.csdn;

import com.aekc.csdn.crawler.Crawler;
import com.aekc.csdn.thread.ThreadPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class Main {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-job.xml");
        Map<String, Crawler> map = applicationContext.getBeansOfType(Crawler.class);
        for(Crawler crawler : map.values()) {
            ThreadPool.runInThread(crawler);
        }
    }
}
