package com.fleetguard360.alert_management.configuration.mapper;

import com.fleetguard360.alert_management.persistence.entity.NivelPrioridad;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NivelPrioridadMapper {
    NivelPrioridadResponse toResponse(NivelPrioridad entity);
    List<NivelPrioridadResponse> toResponses(List<NivelPrioridad> entities);
}
