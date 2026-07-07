package com.fleetguard360.panel_service.configuration;

import com.fleetguard360.panel_service.presentation.dto.AlertInputDTO;
import com.fleetguard360.panel_service.service.interfaces.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertMessageListener {

    private final AlertService alertService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveAlertMessage(AlertInputDTO alertInputDTO) {
        log.info("Received alert message: {}", alertInputDTO);
        try {
            alertService.saveAlert(alertInputDTO);
            log.info("Alert processed and saved successfully");
        } catch (Exception e) {
            log.error("Error processing alert message: {}", e.getMessage(), e);
            throw e;
        }
    }
}

