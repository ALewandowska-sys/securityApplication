package com.springSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

public class Mail {
    private final String text;
    private final String to;
    private final String subject;
    Mail(String text, String to, String subject){
        this.text = text;
        this.to = to;
        this.subject = subject;
    }
    @Value("${spring.mail.username}") String sender;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(){
        java.util.logging.Logger logger = Logger.getLogger(Mail.class.getName());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setSubject(subject);

            javaMailSender.send(mimeMessage);
            logger.info("Mail sent successfully");
        }
        catch (MessagingException e) {
            logger.info("Error while sending mail!");
        }
    }

}
