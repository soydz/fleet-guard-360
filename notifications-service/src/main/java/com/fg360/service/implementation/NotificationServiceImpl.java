package com.fg360.service.implementation;

import com.fg360.presentation.controller.dto.AlertDTO;
import com.fg360.presentation.controller.dto.PushDTO;
import com.fg360.service.interfaces.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Value("${email.sender}")
    private String emailSender;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, SimpMessagingTemplate messagingTemplate) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @RabbitListener(queues = {"${spring.rabbitmq.queue.alert.created.name}"})
    public void handleEmail(AlertDTO alertDTO) {
        handlePush(alertDTO);

        Context context = new Context();
        context.setVariable("tipoAlerta", alertDTO.alertType());
        context.setVariable("responsable", alertDTO.responsible());
        context.setVariable("unidad", alertDTO.generatingUnit());
        context.setVariable("fechaHora", alertDTO.generationDate());

        String html = templateEngine.process("alert-email", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if(emailSender.equals("apikey")){
                helper.setFrom("fabricaescuelafleetguard@gmail.com");
            }
            else{
                helper.setFrom(emailSender);
            }
            helper.setTo(alertDTO.toUsers());
            helper.setSubject("Fleet Guard 360 - " + alertDTO.alertType());
            helper.setText(html, true);

            mailSender.send(message);
            logger.info("Recibido mensaje en la cola 'email_notification_queue': {}", alertDTO);

        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);
        }
    }

    // /app/alert
    @Override
    public void handlePush(AlertDTO alertDTO) {
        PushDTO pushDTO = new PushDTO(
                alertDTO.alertType(),
                alertDTO.generatingUnit(),
                alertDTO.state(),
                alertDTO.generationDate()
        );

        logger.info("Notification Push");
        messagingTemplate.convertAndSend("/all/alerts", pushDTO);
    }
}
