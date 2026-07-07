package com.fleetguard360.alert_management.configuration.mapper;

import com.fleetguard360.alert_management.persistence.entity.TipoAlerta;
import com.fleetguard360.alert_management.presentation.DTO.tipoalerta.TipoAlertaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {NivelPrioridadMapper.class})
public interface TipoAlertaMapper {

    @Mapping(target = "nivelPrioridad", source = "nivelPrioridad")
    TipoAlertaResponse toResponse(TipoAlerta entity);

    List<TipoAlertaResponse> toResponses(List<TipoAlerta> entities);
}
