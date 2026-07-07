package com.fg360.presentation.controller.dto;

import java.time.LocalDateTime;

public record PushDTO(
        String alertType,
        String generatingUnit,
        String statusAlert,
        LocalDateTime generationDate
) {}
