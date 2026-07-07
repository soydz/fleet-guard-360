package com.fleetguard360.panel_service.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AlertInputDTO(
        List<String> toUsers,
        String alertType,
        String responsible,
        String priority,
        String driver,
        String generatingUnit,
        String state,
        LocalDateTime generationDate
) {
}

