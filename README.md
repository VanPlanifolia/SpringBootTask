## SpringBoot中的Task

1.使用异步任务的方式完成邮件发送。在我们实际应用中，经常会用到邮件发送的时候，springboot为我们整合好了邮件发送，这就省的我们写一些繁杂的配置信息，我们只需要在springboot中的配置文件完成邮件信息的配置就可以了。

* 1.1 首先我们需要导入springboot邮件服务的maven依赖以及在配置文件中设置我们的邮箱信息,其中password那一栏需要在qq邮箱里面获取到

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
```

```properties
# 配置邮件发送的信息
spring.mail.host=smtp.qq.com
spring.mail.username=1443176972@qq.com
spring.mail.password=erkdhusapdqvgchi
spring.mail.properties.mail.smtp.ssl.enable=true
```

* 1.2 我们配置好邮箱的配置之后就需要编写发送邮件的工具类了，在工具类中我们直接注入spring中的 JavaMailSender 工具类，然后对于简单邮件我们只需要创建SimpleMailMessage对象，往这个对象里面保存邮件信息就可以了，然后通过JavaMailSender中的send方法将邮件发送出去。而对于含有附件的复杂信件类型，我们则需要创建MimeMessage与MimeMessagHelper对象最后通过javaMailSender来发送mimeMessage对象。

```java
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
```

* 1.3 我们在发送邮件的时候由于网络原因难免会有几秒的延迟，而如果不采用异步任务的话用户看我们的网页就会一直处于白屏状态，这显然是不合理的所以我们可以使用springboot为我们提供的异步任务来完成。首先我们编写Controller层与Service层，然后在service层声明方法为异步方法然后在springboot启动类中添加对应的注解就可以了。

```java
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
```

```java
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

```

```java
@SpringBootApplication
//启动异步任务
@EnableAsync
public class SpringBootTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTaskApplication.class, args);
    }

}
```

* 1.4 然后我们需要编写一个简单的表单来测试一下邮件的发送，注意邮件的发送者一定要与from保持一致！,具体的表单页面在此不再展示。

2.使用定时任务，我们在日常开发情况下经常会涉及到定时任务的应用，springboot为我们提供了定时任务的使用，我们只需要添加两个注解，然后在编写一下cron表达式即可完成定时任务的创建

* 2.1 分别在启动类与我们定时任务的类上添加注解，然后在我们需要设置为定时方法的类上添加注解，并且在注解里面编写cron表达式。注意如果我们不在定时任务的类上添加注解不把他交给spring来管理则无法实现定时任务。

```java
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

```

```java
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
```

* tip 这个定时任务最核心的内容就是cron表达式的编写，牢记他的格式 ，秒 分 时 日 月 周几 年。
  cron表达式转换网站：https://cron.qqe2.com/
