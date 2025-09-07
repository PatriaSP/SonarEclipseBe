package com.patria.apps.config;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {

    @Value("${MAIL_HOST}")
    private String host;

    @Value("${MAIL_PORT}")
    private int port;

    @Value("${MAIL_USERNAME}")
    private String user;

    @Value("${MAIL_PASS}")
    private String pass;

    @Value("${MAIL_PROP_PROTOCOL}")
    private String protocol;

    @Value("${MAIL_PROP_AUTH}")
    private String auth;

    @Value("${MAIL_PROP_STARTTLS}")
    private String tls;

    @Value("${MAIL_PROP_SSL}")
    private String ssl;

    @Value("${MAIL_PROP_DEBUG}")
    private String debug;

    @Value("${MAIL_PROP_TIMEOUT}")
    private String timeout;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(user);
        mailSender.setPassword(pass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", tls);
        props.put("mail.smtp.ssl.enable", ssl);
        props.put("mail.debug", debug);
        props.put("mail.smtp.connectiontimeout", timeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", timeout);

        return mailSender;
    }

}
