package com.scm.scm20.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.scm20.services.EmailService;


@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender eMailSender;

    // @Value("${spring.mail.properties.domain_name}")
    @Value("${spring.mail.username}")
    private String domainName;


    @Override
    public boolean sendEmail(String to, String subject, String body) {



        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);
        eMailSender.send(message);

        return true;

    }




    @Override
    public void sendEmailWithHtml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendEmailWithAttachment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
