package com.fg360.presentation.controller.dto;

public record EmailDTO(
        String[] toUser,
        String subject,
        String message
) {
}
