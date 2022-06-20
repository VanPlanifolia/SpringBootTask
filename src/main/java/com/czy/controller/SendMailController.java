package com.czy.controller;

import com.czy.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.mail.MessagingException;

@Controller
public class SendMailController {
    @Autowired
    SendMailService sendMailService;

    /**
     * 处理发送邮件请求的Controller
     * @param from 发送者
     * @param to 接收者
     * @param subject 主题
     * @param text 正文
     * @return
     */
    @ResponseBody
    @RequestMapping("/senderMail")
    public String sendMail(String from,String to,String subject,String text){
        sendMailService.sendMail(from, to, subject, text);
        return "发送成功！";
    }
    @ResponseBody
    @RequestMapping("/senderComplexMail")
    public String sendComplexMail(String from, String to, String subject, String text){
        try {
            sendMailService.sendCompxMail(from,to,subject,text,"C:\\Users\\14431\\Pictures\\Camera Roll\\Camera Roll\\0.png");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "发送成功！";
    }
}
