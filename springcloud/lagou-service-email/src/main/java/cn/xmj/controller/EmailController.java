/**
 * FileName: EmailController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:33
 * Description:
 */
package cn.xmj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EmailController {

    @Value("${spring.mail.username}")
    private String whoAmI;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送验证码到邮箱，true成功，false失败
     */
    @RequestMapping("/email/{email}/{code}")
    public boolean email(@PathVariable String email, @PathVariable String code) {
        try {
            sendMail(email, code);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void sendMail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("验证码");
        message.setText("您的验证码为：" + content + ", 10分钟内有效");
        message.setFrom(whoAmI);
        mailSender.send(message);
    }
}
