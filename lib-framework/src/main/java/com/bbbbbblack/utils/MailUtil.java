package com.bbbbbblack.utils;

import com.bbbbbblack.domain.entity.Mail;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Data
@Component
public class MailUtil {

    private static JavaMailSender javaMailSender;
    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        MailUtil.javaMailSender = javaMailSender;
    }

    public static void send(Mail mail){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getText());
        message.setFrom(mail.getFrom());
        message.setTo(mail.getTo());
        javaMailSender.send(message);
    }
}
