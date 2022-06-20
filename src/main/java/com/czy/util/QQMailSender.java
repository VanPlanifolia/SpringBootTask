package com.czy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author Planifolia.Van
 * qq邮件发送的工具类，里面封装了简单邮件的发送以及复杂邮件的发送，以及若干重载方法
 */
@Component("qqMailSender")
public class QQMailSender {
    //注入javaMailSender，使用这个对象来完成邮件的发送
    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     * 方法：用于实现简单邮件的发送
     * @param from 发送者
     * @param to 收件人
     * @param subject 主题
     * @param text 正文
     */
    public void simpleMailSender(String from,String to,String subject,String text){
        //创建邮件正文对象
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        //发件人
        simpleMailMessage.setFrom(from);
        //收件人
        simpleMailMessage.setTo(to);
        //设置消息正文，主题等等
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }
    public void simpleMailSender(String to,String subject,String text){
        //创建邮件正文对象
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        //发件人
        simpleMailMessage.setFrom("NULL");
        //收件人
        simpleMailMessage.setTo(to);
        //设置消息正文，主题等等
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        //发送邮件
        javaMailSender.send(simpleMailMessage);
    }
    public void complexMailSender(String from,String to,String subject,String text,String filePath) throws MessagingException {
        //创建复合邮件对象
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        //使用mimeMessageHelp,如果想要返送带附件的复杂邮件需要吧第二个参数设置为true
        MimeMessageHelper mimeHelp=new MimeMessageHelper(mimeMessage,true);
        //王mimeHelp中存值
        mimeHelp.setFrom(from);
        mimeHelp.setTo(to);
        mimeHelp.setSubject(subject);
        mimeHelp.setText(text);
        mimeHelp.addAttachment("1.jpg",new File(filePath));
        //注意发送邮件一定是要使用mimeMessage来发送
        javaMailSender.send(mimeMessage);
    }
}
