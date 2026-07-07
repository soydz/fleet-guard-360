package com.fleetguard360.panel_service.presentation.controller;

import com.fleetguard360.panel_service.presentation.dto.AlertResponseDTO;
import com.fleetguard360.panel_service.service.interfaces.AlertService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AlertController {

    private final AlertService alertService;

    @Value("${panel_base.url}")
    private String baseUrl;
    // Caché efímero: tokens válidos 5 minutos, máximo 100 activos
    private final Cache<String, Boolean> downloadTokens = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .maximumSize(100)
            .build();

    @QueryMapping
    public List<AlertResponseDTO> getAllAlerts() {
        log.info("GraphQL query: getAllAlerts");
        return alertService.getAllAlerts();
    }

    @QueryMapping
    public AlertResponseDTO getAlertById(@Argument Long id) {
        log.info("GraphQL query: getAlertById with id: {}", id);
        return alertService.getAlertById(id);
    }

    @QueryMapping
    public String exportAlertsCsvUrl() {
        String token = UUID.randomUUID().toString();
        downloadTokens.put(token, true);

        return baseUrl + "/export/alerts.csv?token=" + token;
    }

    // Método para validar token (usado en el controller REST)
    boolean isValidToken(String token) {
        return token != null && downloadTokens.getIfPresent(token) != null;
    }
}

