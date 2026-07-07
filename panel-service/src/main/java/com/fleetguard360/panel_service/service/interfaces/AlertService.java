package com.fleetguard360.panel_service.service.interfaces;

import com.fleetguard360.panel_service.presentation.dto.AlertInputDTO;
import com.fleetguard360.panel_service.presentation.dto.AlertResponseDTO;

import java.util.List;

public interface AlertService {
    
    AlertResponseDTO saveAlert(AlertInputDTO alertInputDTO);
    
    List<AlertResponseDTO> getAllAlerts();
    
    AlertResponseDTO getAlertById(Long id);
}

