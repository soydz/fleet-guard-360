package com.fg360.configuration.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host:smtp.gmail.com}")  // Agregar
    private String emailHost;

    @Value("${email.port:465}")  // Cambiar a 465
    private int emailPort;

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");

        return mailSender;
    }
}
