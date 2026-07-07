package com.fleetguard360.alert_management.presentation.DTO.tipoalerta;

import com.fleetguard360.alert_management.persistence.entity.TipoEncargado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TipoAlertaCreateRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,
        @Size(max = 1000, message = "descripcion debe tener máximo 1000 caracteres")
        String descripcion,
        @NotNull(message = "nivelPrioridadId es obligatorio")
        @Positive(message = "nivelPrioridadId debe ser un número positivo")
        Integer nivelPrioridadId,
        @NotNull(message = "tipoEncargado es obligatorio")
        TipoEncargado tipoEncargado
) {}
