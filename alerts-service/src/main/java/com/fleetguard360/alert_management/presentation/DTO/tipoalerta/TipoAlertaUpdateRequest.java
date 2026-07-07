package com.fleetguard360.alert_management.presentation.DTO.tipoalerta;

import com.fleetguard360.alert_management.persistence.entity.TipoEncargado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TipoAlertaUpdateRequest(
        @NotBlank(message = "nombre no puede ser vacío")
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,
        @Size(max = 1000, message = "descripcion debe tener máximo 1000 caracteres")
        String descripcion,
        @Positive(message = "nivelPrioridadId debe ser un número positivo")
        Integer nivelPrioridadId,
        TipoEncargado tipoEncargado
) {}
