package com.czy.service;

import com.czy.util.QQMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class SendMailService {
    @Autowired
    QQMailSender qqMailSender;
    //异步任务
    @Async
    public void sendMail(String from,String to,String subject,String text){
        System.out.println("正在发送，请稍后");
        qqMailSender.simpleMailSender(from, to, subject, text);
    }
    //异步任务
    @Async
    public void sendCompxMail(String from,String to,String subject,String text,String filePath) throws MessagingException {
        System.out.println("正在发送，请稍后");
        qqMailSender.complexMailSender(from, to, subject, text, filePath);
    }
}
