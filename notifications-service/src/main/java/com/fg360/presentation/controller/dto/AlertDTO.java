package com.fg360.presentation.controller.dto;

import java.time.LocalDateTime;

public record AlertDTO(
        String[] toUsers,
        String alertType,
        String responsible,
        String priority,
        String driver,
        String generatingUnit,
        String state,
        LocalDateTime generationDate
) {
}
