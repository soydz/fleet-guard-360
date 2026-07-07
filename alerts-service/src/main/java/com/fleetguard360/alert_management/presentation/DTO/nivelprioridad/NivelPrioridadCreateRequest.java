package com.fleetguard360.alert_management.presentation.DTO.nivelprioridad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NivelPrioridadCreateRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 50, message = "nombre debe tener máximo 50 caracteres")
        String nombre
) {}
