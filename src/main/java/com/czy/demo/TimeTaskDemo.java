package com.czy.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * springboot中的定时任务
 */
@Component("timeTaskDemo")
public class TimeTaskDemo {
    //声明这是一个定时任务，并且在cron写执行的表达式
    // 秒 分 时 日 月 周几
    @Scheduled(cron = "* * * * * ?")
    public void timedTask(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatdate = sdf.format(date);
        System.out.println("程序运行了！" + formatdate);
    }
}
