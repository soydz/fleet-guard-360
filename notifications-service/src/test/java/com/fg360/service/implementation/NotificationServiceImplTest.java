package com.fg360.service.implementation;

import com.fg360.presentation.controller.dto.AlertDTO;
import com.fg360.presentation.controller.dto.PushDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests para NotificationServiceImpl
 * Cubre los diferentes caminos del envío de correo y notificaciones push.
 */
class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private AlertDTO alert;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        alert = new AlertDTO(
                new String[]{"test@fg360.com"},
                "Alerta de temperatura",
                "Juan Pérez",
                "Alta",
                "Carlos Rivas",
                "Unidad 01",
                "Activa",
                LocalDateTime.now()
        );

        // Inyectamos manualmente el valor del sender para evitar NullPointerException
        Field emailSenderField = NotificationServiceImpl.class.getDeclaredField("emailSender");
        emailSenderField.setAccessible(true);
        emailSenderField.set(notificationService, "apikey");
    }

    /**
     * Caso 1: Sender = "apikey"
     * Debe enviar correo y notificación push correctamente.
     */
    @Test
    void handleEmail_sendsMailAndPush_withApiKeySender() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("alert-email"), any(Context.class)))
                .thenReturn("<html>Mock Template</html>");

        notificationService.handleEmail(alert);

        verify(templateEngine, times(1)).process(eq("alert-email"), any(Context.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/all/alerts"), any(PushDTO.class));
    }

    /**
     * Caso 2: Sender personalizado distinto de "apikey"
     */
    @Test
    void handleEmail_sendsMailWithCustomSender() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html></html>");

        // Cambiamos el sender a uno diferente
        Field emailSenderField = NotificationServiceImpl.class.getDeclaredField("emailSender");
        emailSenderField.setAccessible(true);
        emailSenderField.set(notificationService, "custom@fg360.com");

        notificationService.handleEmail(alert);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("alert-email"), any(Context.class));
    }

    /**
     * Caso 3: Simula una excepción al enviar correo (MessagingException)
     * Debe atrapar el error y continuar sin lanzar excepción.
     */
    @Test
    void handleEmail_whenMailSendFails_shouldLogError() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html></html>");

        // Simulamos el fallo en el envío de correo sin usar doThrow (evita MockitoException)
        doAnswer(invocation -> {
            throw new MessagingException("Simulated Error");
        }).when(mailSender).send(any(MimeMessage.class));

        notificationService.handleEmail(alert);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/all/alerts"), any(PushDTO.class));
    }


    /**
     * Caso 4: Notificación Push directa sin correo
     */
    @Test
    void handlePush_shouldSendWebsocketNotification() {
        notificationService.handlePush(alert);

        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/all/alerts"), any(PushDTO.class));
    }

    /**
     * Caso 5: Sin destinatarios, aún debe procesar plantilla y enviar
     */
    @Test
    void handleEmail_withEmptyRecipients_shouldStillProcessTemplate() throws Exception {
        AlertDTO alertWithoutRecipients = new AlertDTO(
                new String[]{},
                "Sin destinatarios",
                "Responsable",
                "Alta",
                "Conductor",
                "Unidad X",
                "Activa",
                LocalDateTime.now()
        );

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html></html>");

        notificationService.handleEmail(alertWithoutRecipients);

        verify(templateEngine, times(1)).process(eq("alert-email"), any(Context.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Caso 6: Plantilla vacía (no null), aún se intenta enviar correo
     */
    @Test
    void handleEmail_withEmptyTemplateOutput_shouldStillAttemptToSend() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("");

        notificationService.handleEmail(alert);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Caso 7 (extra): Verifica que se construyan correctamente las variables Thymeleaf
     */
    @Test
    void handleEmail_shouldPopulateTemplateVariables() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("alert-email"), any(Context.class))).thenAnswer(invocation -> {
            Context ctx = invocation.getArgument(1);
            assert ctx.getVariable("tipoAlerta").equals("Alerta de temperatura");
            assert ctx.getVariable("responsable").equals("Juan Pérez");
            return "<html>ok</html>";
        });

        notificationService.handleEmail(alert);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
