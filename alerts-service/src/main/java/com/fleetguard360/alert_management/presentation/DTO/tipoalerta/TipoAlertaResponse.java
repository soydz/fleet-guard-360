package com.fleetguard360.alert_management.presentation.DTO.tipoalerta;

import com.fleetguard360.alert_management.persistence.entity.TipoEncargado;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadResponse;

public record TipoAlertaResponse(
        Integer id,
        String nombre,
        String descripcion,
        NivelPrioridadResponse nivelPrioridad,
        TipoEncargado tipoEncargado
) {}
