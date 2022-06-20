package com.czy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//启动异步任务
@EnableAsync
//开启定时任务
@EnableScheduling
public class SpringBootTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTaskApplication.class, args);
    }}
