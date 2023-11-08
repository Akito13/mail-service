package com.example.bookshop.mailservice.service;

import com.example.bookshop.mailservice.model.MailInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender sender;

    @Autowired
    public MailService( JavaMailSender javaMailSender) {
        this.sender = javaMailSender;
    }

    @Async
    public void send(MailInfo mail) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
        helper.setTo(InternetAddress.parse(mail.getTo()));
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(),true);
        String[] cc = mail.getCc();
        if(cc != null && cc.length >0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if(bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        sender.send(message);
    }
    public void send(String to, String subject, String body) throws MessagingException {
        this.send(MailInfo.builder()
                .to(to)
                .subject(subject)
                .body(body).build());
    }
}
