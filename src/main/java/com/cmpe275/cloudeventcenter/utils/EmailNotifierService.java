package com.cmpe275.cloudeventcenter.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailNotifierService{

    @Autowired
    private JavaMailSender javaMailSender;

    public void notify(String to,
                       String subject,
                       String body){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        try {
            simpleMailMessage.setFrom("cec.notifications.275@gmail.com");
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);

            System.out.println("Mail sent successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
