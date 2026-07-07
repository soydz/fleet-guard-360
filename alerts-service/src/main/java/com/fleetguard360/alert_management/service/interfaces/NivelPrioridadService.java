package com.fleetguard360.alert_management.service.interfaces;

import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadCreateRequest;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadResponse;
import com.fleetguard360.alert_management.presentation.DTO.nivelprioridad.NivelPrioridadUpdateRequest;

import java.util.List;

public interface NivelPrioridadService {
    NivelPrioridadResponse create(NivelPrioridadCreateRequest request);
    NivelPrioridadResponse update(Integer id, NivelPrioridadUpdateRequest request);
    void delete(Integer id);
    NivelPrioridadResponse getById(Integer id);
    List<NivelPrioridadResponse> listAll();
}
