package com.patria.apps.helper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailHelperService {
    
    @Autowired
    private JavaMailSender emailSender;
    
    @Value("${MAIL_USERNAME}")
    private String senderMail;
    
    @Value("${MAIL_NAME_SENDER}")
    private String mailNameSender;
    
    public Map<String,String> send(String to, String mailMessage, String subject){
        Map<String,String> ret = new HashMap<String,String>();
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailMessage, true);
            helper.setFrom(new InternetAddress(senderMail, mailNameSender));
            emailSender.send(message);
        } catch (MessagingException ex) {
            ret.put("Status", "Failed");
            ret.put("Message", ex.getMessage());
            return ret;
        } catch (UnsupportedEncodingException ex) {
            ret.put("Status", "Failed");
            ret.put("Message", ex.getMessage());
            return ret;
        }
        ret.put("Status", "Success");
        ret.put("Message", "Mail Send");
        return ret;
    }
    
    public Map<String,String> send(String[] to, String mailMessage, String subject){
        Map<String,String> ret = new HashMap<String,String>();
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailMessage, true);
            helper.setFrom(new InternetAddress(senderMail, mailNameSender));
            emailSender.send(message);
        } catch (MessagingException ex) {
            ret.put("Status", "Failed");
            ret.put("Message", ex.getMessage());
            return ret;
        } catch (UnsupportedEncodingException ex) {
            ret.put("Status", "Failed");
            ret.put("Message", ex.getMessage());
            return ret;
        }
        ret.put("Status", "Success");
        ret.put("Message", "Mail Send");
        return ret;
    }
}
