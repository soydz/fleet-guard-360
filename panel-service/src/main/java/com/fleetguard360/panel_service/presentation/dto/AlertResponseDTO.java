package com.fleetguard360.panel_service.presentation.dto;

import java.time.LocalDateTime;

public record AlertResponseDTO(
        Long id,
        String alertType,
        String responsible,
        String priority,
        String driver,
        String generatingUnit,
        String state,
        LocalDateTime generationDate
) {
}
