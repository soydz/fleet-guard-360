package com.fleetguard360.panel_service.service.implementation;

import com.fleetguard360.panel_service.persistence.entity.Alert;
import com.fleetguard360.panel_service.persistence.repository.AlertRepository;
import com.fleetguard360.panel_service.presentation.dto.AlertInputDTO;
import com.fleetguard360.panel_service.presentation.dto.AlertResponseDTO;
import com.fleetguard360.panel_service.service.exception.AlertNotFoundException;
import com.fleetguard360.panel_service.service.interfaces.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    @Transactional
    public AlertResponseDTO saveAlert(AlertInputDTO alertInputDTO) {
        log.info("Saving new alert of type: {}", alertInputDTO.alertType());

        Alert alert = new Alert();
        alert.setAlertType(alertInputDTO.alertType());
        alert.setResponsible(alertInputDTO.responsible());
        alert.setPriority(alertInputDTO.priority());
        alert.setDriver(alertInputDTO.driver());
        alert.setGeneratingUnit(alertInputDTO.generatingUnit());
        alert.setState(alertInputDTO.state());
        alert.setGenerationDate(alertInputDTO.generationDate());

        Alert savedAlert = alertRepository.save(alert);
        log.info("Alert saved successfully with id: {}", savedAlert.getId());

        return mapToResponseDTO(savedAlert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertResponseDTO> getAllAlerts() {
        log.info("Fetching all alerts");
        return alertRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponseDTO getAlertById(Long id) {
        log.info("Fetching alert with id: {}", id);
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));
        return mapToResponseDTO(alert);
    }

    private AlertResponseDTO mapToResponseDTO(Alert alert) {
        return new AlertResponseDTO(
                alert.getId(),
                alert.getAlertType(),
                alert.getResponsible(),
                alert.getPriority(),
                alert.getDriver(),
                alert.getGeneratingUnit(),
                alert.getState(),
                alert.getGenerationDate()
        );
    }
}
